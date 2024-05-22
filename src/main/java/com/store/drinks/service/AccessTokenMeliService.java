
package com.store.drinks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.drinks.entidade.AccessTokenMeli;
import com.store.drinks.entidade.dto.AccessTokenMeliDTO;
import com.store.drinks.repository.AccessTokenMeliRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class AccessTokenMeliService {
  
  @Autowired
  private AccessTokenMeliRepository accessTokenMeliRepository;
  
  @Value("${url.token}")
  private String urlToken;
  
  @Value("${app.id}")
  private String appId;
  
  @Value("${app.secret}")
  private String appSecret;
  
  @Transactional
  public AccessTokenMeli salvar(AccessTokenMeli accessTokenMeli) {
    accessTokenMeli = this.accessTokenMeliRepository.save(accessTokenMeli);
    return accessTokenMeli;
  }
  
  public AccessTokenMeliDTO atualAccessTokenMeli() {
    try {
      
      var tenant = UsuarioService.usuarioLogado().getClienteSistema().getTenant();
      
      var meliToken = accessTokenMeliRepository.findOne(Example.of(AccessTokenMeli.of(tenant))).get();
      
      RestTemplate restTemplate = new RestTemplate();
      
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
      headers.add("Accept", MediaType.APPLICATION_JSON.toString());
      
      MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
      requestBody.add("grant_type", "refresh_token");
      requestBody.add("client_id", appId);
      requestBody.add("client_secret", appSecret);
      requestBody.add("refresh_token", meliToken.getRefreshToken());
      
      HttpEntity formEntity = new HttpEntity<>(requestBody, headers);
      var response = restTemplate.exchange(urlToken, HttpMethod.POST, formEntity, String.class);
      ObjectMapper objectMapper = new ObjectMapper();
      var accessTokenMeliDTO = objectMapper.readValue(response.getBody(), AccessTokenMeliDTO.class);
      BeanUtils.copyProperties(accessTokenMeliDTO, meliToken, "id");
      
      salvar(meliToken);
      
      BeanUtils.copyProperties(meliToken, accessTokenMeliDTO);
      
      return accessTokenMeliDTO;
      
    } catch (JsonProcessingException | BeansException | RestClientException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
  }
}

package com.store.drinks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.drinks.entidade.MercadoLivre;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.AccessTokenMeli;
import com.store.drinks.entidade.dto.meli.AccessTokenMelidto;
import com.store.drinks.entidade.dto.meli.RevogarContaMelidto;
import com.store.drinks.entidade.enuns.PlataformasEcommerce;
import com.store.drinks.repository.AccessTokenMeliRepository;
import com.store.drinks.repository.MercadoLivreRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class MercadoLivreService {
  
  private MercadoLivreRepository mercadoLivreRepository;
  private PlataformasIntegracaoService plataformasIntegracaoService;
  private UsuarioService usuarioService;
  private AccessTokenMeliRepository accessTokenMeliRepository;
  
  @Value("${meli.url.token}")
  private String urlToken;
  
  @Value("${meli.url.redirect}")
  private String redirect;
  
  @Value("${meli.app.id}")
  private String appId;
  
  @Value("${meli.app.secret}")
  private String appSecret;
  
  @Value("${meli.url.delete.user}")
  private String urlDeleteUser;

  @Autowired
  public MercadoLivreService(
    MercadoLivreRepository mercadoLivreRepository, 
    PlataformasIntegracaoService plataformasIntegracaoService, 
    UsuarioService usuarioService, 
    AccessTokenMeliRepository accessTokenMeliRepository
  )
  {
    this.mercadoLivreRepository = mercadoLivreRepository;
    this.plataformasIntegracaoService = plataformasIntegracaoService;
    this.usuarioService = usuarioService;
    this.accessTokenMeliRepository = accessTokenMeliRepository;
  }
  
  @Transactional
  public void salvar(String code) {
    var usuario = UsuarioService.usuarioLogado();
    if(usuario.getProprietario() && usuario.getAdm()&& plataformaImplementada(usuario) == Boolean.FALSE) {
      var mercadoLivre = new MercadoLivre();
      mercadoLivre.setCode(code);
      mercadoLivre.setUsuario(usuario);
      var plataformaIntegracao = plataformasIntegracaoService.getByDescricao(PlataformasEcommerce.MERCADO_LIVRE.descricao());
      plataformaIntegracao.setImplantada(Boolean.TRUE);
      plataformasIntegracaoService.salvar(plataformaIntegracao);
      mercadoLivre.setPlataformasEcommerce(plataformaIntegracao);
      mercadoLivreRepository.save(mercadoLivre);
      usuarioService.updateUsuarioMercadoLivre(mercadoLivre);
      return;
    }
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Essa etapa ja foi concluída!");
  }
  
  @Transactional
  public void salvarTokenMercadoLivre() {
    try {
      RestTemplate restTemplate = new RestTemplate();

      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
      headers.add("Accept", MediaType.APPLICATION_JSON.toString());

      MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
      requestBody.add("grant_type", "authorization_code");
      requestBody.add("client_id", appId);
      requestBody.add("client_secret", appSecret);
      requestBody.add("code", usuarioService.usuarioAtualizado().getMercadoLivre().getCode());
      requestBody.add("redirect_uri", redirect);

      HttpEntity formEntity = new HttpEntity<>(requestBody, headers);
      var response = restTemplate.exchange(urlToken, HttpMethod.POST, formEntity, String.class);
      ObjectMapper objectMapper = new ObjectMapper();
      var accessTokenMeliDTO = objectMapper.readValue(response.getBody(), AccessTokenMelidto.class);
      var accessTokenMeli = new AccessTokenMeli();
      BeanUtils.copyProperties(accessTokenMeliDTO, accessTokenMeli);
      accessTokenMeli.setMercadoLivre(usuarioService.usuarioAtualizado().getMercadoLivre());
      salvarAccessTokenMeli(accessTokenMeli);
    } catch (JsonProcessingException | BeansException | RestClientException ex) {
      var usuario = usuarioService.usuarioAtualizado();
      var mercadoLivre = usuario.getMercadoLivre();
      usuario.setMercadoLivre(null);
      usuarioService.salvar(usuario);
      var plataformaIntegracao = plataformasIntegracaoService.getByDescricao(PlataformasEcommerce.MERCADO_LIVRE.descricao());
      plataformaIntegracao.setImplantada(Boolean.FALSE);
      plataformasIntegracaoService.salvar(plataformaIntegracao);
      mercadoLivreRepository.deleteById(mercadoLivre.getId());
      log.error(ex.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Um erro ocorreu ao tentar concluir o processo de integração, saia da aplicação e entre novamente, refaça todo o processo do zero!");
    }
  }
  
  @Transactional
  public AccessTokenMeli salvarAccessTokenMeli(AccessTokenMeli accessTokenMeli) {
    accessTokenMeli = this.accessTokenMeliRepository.save(accessTokenMeli);
    return accessTokenMeli;
  }
  
  public AccessTokenMelidto atualAccessTokenMeli() {
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
      
      ResponseEntity<AccessTokenMelidto> response = restTemplate.exchange(urlToken, HttpMethod.POST, formEntity, AccessTokenMelidto.class); //new ParameterizedTypeReference<List<CategoriaMercadoLivredto>>() {}
      AccessTokenMelidto accessTokenMelidto = response.getBody();
      BeanUtils.copyProperties(accessTokenMelidto, meliToken, "id");
      salvarAccessTokenMeli(meliToken);
      BeanUtils.copyProperties(meliToken, accessTokenMelidto);
      return accessTokenMelidto;
    } catch (BeansException | RestClientException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
  }
  
  @Transactional
  public void desvincularContaMercadoLivre() {
    try {
      var usuario = usuarioService.usuarioAtualizado();
      if(usuario.getMercadoLivre() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível fazer a desvinculação");
      }
      atualAccessTokenMeli();
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", "Bearer "+usuario.getMercadoLivre().getAccessTokenMeli().getAccessToken());
      HttpEntity formEntity = new HttpEntity<>(headers);

      Map<String, String> vars = new HashMap<>();
      vars.put("userid", usuario.getMercadoLivre().getAccessTokenMeli().getUserId().toString());

      ResponseEntity<RevogarContaMelidto> responseEntity = restTemplate.exchange(urlDeleteUser, HttpMethod.DELETE, formEntity, RevogarContaMelidto.class, vars);
      RevogarContaMelidto revogarContaMelidto = responseEntity.getBody();
      if(revogarContaMelidto.getMsg().toLowerCase().contains("Autorización eliminada".toLowerCase())) {
        var mercadoLivre = usuario.getMercadoLivre();
        usuario.setMercadoLivre(null);
        usuarioService.salvar(usuario);
        accessTokenMeliRepository.deleteById(mercadoLivre.getAccessTokenMeli().getId());
        var plataformaIntegracao = plataformasIntegracaoService.getByDescricao(PlataformasEcommerce.MERCADO_LIVRE.descricao());
        plataformaIntegracao.setImplantada(Boolean.FALSE);
        plataformasIntegracaoService.salvar(plataformaIntegracao);
        mercadoLivreRepository.deleteById(mercadoLivre.getId());
      }
    } catch (RestClientException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    
  }
  
  private boolean plataformaImplementada(Usuario usuario) {
    if (usuario.getMercadoLivre() != null) {
      return usuario.getMercadoLivre().getPlataformasEcommerce().getImplantada();
    }
    return false;
  }
  
  public void verificarSeMercadoFoiImplementado() {
    var mercadoLivre = UsuarioService.usuarioLogado().getMercadoLivre();
    if(Objects.nonNull(mercadoLivre) && mercadoLivre.getId() > 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Plataforma Mercado Livre ja foi implantada");
    }
  }
}

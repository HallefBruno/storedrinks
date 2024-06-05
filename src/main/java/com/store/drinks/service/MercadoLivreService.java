package com.store.drinks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.drinks.entidade.MercadoLivre;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.AccessTokenMeli;
import com.store.drinks.entidade.dto.meli.AccessTokenMelidto;
import com.store.drinks.entidade.enuns.PlataformasEcommerce;
import com.store.drinks.repository.MercadoLivreRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Service
public class MercadoLivreService {
  
  private MercadoLivreRepository mercadoLivreRepository;
  private PlataformasIntegracaoService plataformasIntegracaoService;
  private UsuarioService usuarioService;
  private AccessTokenMeliService accessTokenMeliService;
  
  @Value("${url.token}")
  private String urlToken;
  
  @Value("${url.redirect}")
  private String redirect;
  
  @Value("${app.id}")
  private String appId;
  
  @Value("${app.secret}")
  private String appSecret;

  @Autowired
  public MercadoLivreService(MercadoLivreRepository mercadoLivreRepository, PlataformasIntegracaoService plataformasIntegracaoService, UsuarioService usuarioService, AccessTokenMeliService accessTokenMeliService) {
    this.mercadoLivreRepository = mercadoLivreRepository;
    this.plataformasIntegracaoService = plataformasIntegracaoService;
    this.usuarioService = usuarioService;
    this.accessTokenMeliService = accessTokenMeliService;
  }
  
  @Transactional
  public void salvar(String code) {
    var usuario = UsuarioService.usuarioLogado();
    if(usuario.getProprietario() && plataformaImplementada(usuario) == Boolean.FALSE) {
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
      accessTokenMeliService.salvar(accessTokenMeli);
    } catch (JsonProcessingException | BeansException | RestClientException ex) {
      var usuario = usuarioService.usuarioAtualizado();
      var mercadoLivre = usuario.getMercadoLivre();
      usuario.setMercadoLivre(null);
      usuarioService.salvar(usuario);
      var plataformaIntegracao = plataformasIntegracaoService.getByDescricao(PlataformasEcommerce.MERCADO_LIVRE.descricao());
      plataformaIntegracao.setImplantada(Boolean.FALSE);
      plataformasIntegracaoService.salvar(plataformaIntegracao);
      mercadoLivreRepository.deleteById(mercadoLivre.getId());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Um erro ocorreu ao tentar concluir o processo de integração, saia da aplicação e entre novamente, refaça todo o processo do zero!");
    }
  }
  
  private boolean plataformaImplementada(Usuario usuario) {
    if (usuario.getMercadoLivre() != null) {
      return usuario.getMercadoLivre().getPlataformasEcommerce().getImplantada();
    }
    return false;
  }
}

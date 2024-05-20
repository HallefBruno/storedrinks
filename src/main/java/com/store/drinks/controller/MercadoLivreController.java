package com.store.drinks.controller;

import com.store.drinks.service.MercadoLivreService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/mercado-livre")
@PreAuthorize("hasRole('INTEGRACOES')")
public class MercadoLivreController {
  
  @Value("${url.meli.code}")
  private String urlMeliCode;
  
  @Value("${url.token}")
  private String urlToken;
  
  @Value("${url.redirect}")
  private String redirect;
  
  @Value("${app.id}")
  private String appId;
  
  @Value("${app.secret}")
  private String appSecret;
  
  @Autowired
  private MercadoLivreService mercadoLivreService;
  
  @GetMapping("/code")
  public ResponseEntity<String> urlForRedirect(HttpServletRequest request) {
    String s = request.getHeader("User-Agent");
    if(s.toLowerCase().contains("opr") || s.toLowerCase().contains("opera")) {
      return ResponseEntity.badRequest().body(s);
    }
    return ResponseEntity.ok(urlMeliCode);
  }
  
  @GetMapping("/redirect-code")
  public String redirecCode(@RequestParam("code") String code) {
    mercadoLivreService.salvar(code);
    return "redirect:/congratulation";
  }

  @GetMapping("/token")
  public ResponseEntity<?> token() {
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
    headers.add("Accept", MediaType.APPLICATION_JSON.toString());

    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("grant_type", "authorization_code");
    requestBody.add("client_id", appId);
    requestBody.add("client_secret", appSecret);
    requestBody.add("code", "TG-664625cf76c85000019633d6-560901384");
    //requestBody.add("redirect_uri", redirect);

    HttpEntity formEntity = new HttpEntity<>(requestBody, headers);
    ResponseEntity<?> response = restTemplate.exchange(urlToken, HttpMethod.POST, formEntity, String.class);
    return ResponseEntity.ok(response.getBody());
  }

  @GetMapping("/refresh-token")
  public ResponseEntity<?> refreshToken() {
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
    headers.add("Accept", MediaType.APPLICATION_JSON.toString());

    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("grant_type", "refresh_token");
    requestBody.add("client_id", appId);
    requestBody.add("client_secret", appSecret);
    requestBody.add("refresh_token", "TG-664626667f781d0001e928b4-560901384");

    HttpEntity formEntity = new HttpEntity<>(requestBody, headers);
    ResponseEntity<?> response = restTemplate.exchange(urlToken, HttpMethod.POST, formEntity, String.class);
    return ResponseEntity.ok(response.getBody());
  }

}

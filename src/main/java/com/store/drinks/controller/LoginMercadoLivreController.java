
package com.store.drinks.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/integracoes")
public class LoginMercadoLivreController {
    
    @GetMapping
    public ModelAndView page() {
        return new ModelAndView("integracoes/Plataformas");
    }
    
    @GetMapping("/token")
    public ResponseEntity<?> getToken() {
        
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
        headers.add("Accept", MediaType.APPLICATION_JSON.toString()); //Optional in case server sends back JSON data

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type",  "authorization_code");
        requestBody.add("client_id", "2035043297707429");
        requestBody.add("client_secret",  "q6Yl9pF4xm3L5fZhQmPoRqIO7oEbkDxW");
        requestBody.add("code", "TG-664625cf76c85000019633d6-560901384");
        requestBody.add("redirect_uri", "https://google.com.br");

        HttpEntity formEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<?> response = restTemplate.exchange("https://api.mercadolibre.com/oauth/token", HttpMethod.POST, formEntity, String.class);
        return ResponseEntity.ok(response.getBody());
    }
    
    @GetMapping("refresh-token")
    public ResponseEntity<?> getAccessToken() {
        
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type",  "refresh_token");
        requestBody.add("client_id", "2035043297707429");
        requestBody.add("client_secret",  "q6Yl9pF4xm3L5fZhQmPoRqIO7oEbkDxW");
        requestBody.add("refresh_token",  "TG-664626667f781d0001e928b4-560901384");

        HttpEntity formEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<?> response = restTemplate.exchange("https://api.mercadolibre.com/oauth/token", HttpMethod.POST, formEntity, String.class);
        return ResponseEntity.ok(response.getBody());
    }
    
}

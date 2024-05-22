package com.store.drinks.controller;

import com.store.drinks.service.AccessTokenMeliService;
import com.store.drinks.service.MercadoLivreService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mercado-livre")
@PreAuthorize("hasRole('INTEGRACOES')")
public class MercadoLivreController {
  
  @Value("${url.meli.code}")
  private String urlMeliCode;
  
  @Autowired
  private MercadoLivreService mercadoLivreService;
  
  @Autowired
  private AccessTokenMeliService accessTokenMeliService;
  
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
    return "redirect:/mercado-livre/token";
  }

  @GetMapping("/token")
  public String token() {
    mercadoLivreService.salvarTokenMercadoLivre();
    return "redirect:/congratulation";
  }

  @GetMapping("/refresh-token")
  public ResponseEntity<?> refreshToken() {
    return ResponseEntity.ok(accessTokenMeliService.atualAccessTokenMeli());
  }

}

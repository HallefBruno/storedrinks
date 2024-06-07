package com.store.drinks.controller;

import com.store.drinks.entidade.CategoriaMercadoLivre;
import com.store.drinks.service.CategoriaMercadoLivreService;
import com.store.drinks.service.MercadoLivreService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mercado-livre")
//@PreAuthorize("hasRole('MERCADO_LIVRE')")
public class MercadoLivreController extends EncerrarSessaoController {
  
  @Autowired
  private CategoriaMercadoLivreService categoriaMercadoLivreService;
  
  @Autowired
  private MercadoLivreService mercadoLivreService;
  
  @Value("${meli.url.code}")
  private String urlMeliCode;
  
  @GetMapping("/code")
  public ResponseEntity<String> urlForRedirect(HttpServletRequest request) {
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
    return ResponseEntity.ok(mercadoLivreService.atualAccessTokenMeli());
  }
  
  @DeleteMapping("/desvincular-conta-meli")
  public ResponseEntity<?> desvincularContaMeli(HttpServletRequest request, HttpServletResponse response) {
    try {
      mercadoLivreService.desvincularContaMercadoLivre();
      return ResponseEntity.ok().build();
    } catch (ResponseStatusException ex) {
      throw new ResponseStatusException(ex.getStatus(), ex.getReason());
    }
  }
  
  @GetMapping("/login")
  public void login(HttpServletRequest request, HttpServletResponse response) {
     encerrarSessao(request, response);
  }
  
  @GetMapping("/page-categorias")
  public ModelAndView pageCategorias(CategoriaMercadoLivre categoriaMercadoLivre) {
    ModelAndView modelAndView = new ModelAndView("mercadolivre/Categorias");
    modelAndView.addObject("categorias", categoriaMercadoLivreService.todas());
    return modelAndView;
  }

  @GetMapping("/atualizar-categorias")
  public ModelAndView categorias(CategoriaMercadoLivre categoriaMercadoLivre, BindingResult result, RedirectAttributes attributes) {
    try {
      categoriaMercadoLivreService.salvar();
    } catch (ResponseStatusException ex) {
      ObjectError error = new ObjectError("erro", ex.getReason());
      result.addError(error);
      return pageCategorias(categoriaMercadoLivre);
    }
    attributes.addFlashAttribute("mensagem", "Atualização realizada com sucesso!");
    return new ModelAndView("redirect:/mercado-livre/page-categorias");
  }
  
  @GetMapping("/page-desvincular-conta-meli")
  public ModelAndView pageDesvincularContaMeli() {
    ModelAndView modelAndView = new ModelAndView("mercadolivre/DesvincularContaMeli");
    return modelAndView;
  }
  
}

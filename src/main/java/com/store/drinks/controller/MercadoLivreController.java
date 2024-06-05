package com.store.drinks.controller;

import com.store.drinks.entidade.CategoriaMercadoLivre;
import com.store.drinks.service.CategoriaMercadoLivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mercado-livre")
//@PreAuthorize("hasRole('MERCADO_LIVRE')")
public class MercadoLivreController {
  
  @Autowired
  private CategoriaMercadoLivreService categoriaMercadoLivreService;
  
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
  
  @DeleteMapping("/desvincular-conta-meli")
  public void desvincularContaMeli() {
    
  }
  
}


package com.store.drinks.controller;

import com.store.drinks.service.PlataformasIntegracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/integracoes")
public class PlataformasEcommerceController {
  
  @Autowired
  private PlataformasIntegracaoService plataformasIntegracaoService;
  
  @GetMapping
  public ModelAndView page() {
    ModelAndView modelAndView = new ModelAndView("integracoes/Plataformas");
    modelAndView.addObject("plataformasAtivas", plataformasIntegracaoService.todasAtivas());
    return modelAndView;
  }
}

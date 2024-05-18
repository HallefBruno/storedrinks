
package com.store.drinks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/integracoes")
public class IntentegracoesController {
  
  @GetMapping
  public ModelAndView page() {
    return new ModelAndView("integracoes/Plataformas");
  }
}

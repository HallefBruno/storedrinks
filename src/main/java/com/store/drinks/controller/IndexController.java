package com.store.drinks.controller;

import com.store.drinks.service.MovimentacaoCaixaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
  
  @Autowired
  private MovimentacaoCaixaService movimentacaoCaixaService;
  
  @RequestMapping("/")
  public ModelAndView index() {
    movimentacaoCaixaService.salvar();
    return new ModelAndView("Dashboard");
  }

}

package com.store.drinks.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/movimentacao-caixa")
@RequiredArgsConstructor
public class MovimentacaoCaixaController {
  
  @GetMapping
  public ModelAndView pageIndex() {
    return new ModelAndView("movimentacaocaixa/MovimentacaoCaixa");
  }
}

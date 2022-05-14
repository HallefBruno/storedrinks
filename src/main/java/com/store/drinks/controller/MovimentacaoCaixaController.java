package com.store.drinks.controller;

import com.store.drinks.repository.querys.movimentacaoCaixa.MovimentacoesCaixaFilters;
import com.store.drinks.service.MovimentacaoCaixaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/movimentacao-caixa")
@RequiredArgsConstructor
public class MovimentacaoCaixaController {
  
  private final MovimentacaoCaixaService movimentacaoCaixaService;
  
  @GetMapping
  public ModelAndView pageIndex() {
    movimentacaoCaixaService.movimentacoesCaixa(new MovimentacoesCaixaFilters(), 0, 0);
    return new ModelAndView("movimentacaocaixa/MovimentacaoCaixa");
  }
}

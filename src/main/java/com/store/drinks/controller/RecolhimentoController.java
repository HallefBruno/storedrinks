package com.store.drinks.controller;

import com.store.drinks.service.MovimentacaoCaixaService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/recolhimento")
@RequiredArgsConstructor
public class RecolhimentoController {
  
  private final MovimentacaoCaixaService movimentacaoCaixaService;
  
  @GetMapping
  public ModelAndView pageIndex() {
    return new ModelAndView("recolhimento/Recolhimento");
  }
  
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void salvar(@RequestParam(required = true, name = "valorRetirada") BigDecimal valorRetirada) {
    movimentacaoCaixaService.salvar(valorRetirada);
  }
  
}

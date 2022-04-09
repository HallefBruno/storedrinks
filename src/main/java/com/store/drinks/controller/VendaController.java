

package com.store.drinks.controller;

import com.store.drinks.service.AbrirCaixaService;
import com.store.drinks.service.VendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/vendas")
@RequiredArgsConstructor
public class VendaController {
  
  private final AbrirCaixaService abrirCaixaService;
  private final VendaService vendaService;
  
  @GetMapping
  public ModelAndView pageIndex() {
    if (!abrirCaixaService.abrirCaixaPorUsuarioLogado()) {
      return new ModelAndView("redirect:/pdv/abrirCaixa");
    }
    return new ModelAndView("venda/RealizarVenda");
  }
  
}


package com.store.drinks.controller;

import com.store.drinks.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/estoque")
@RequiredArgsConstructor
public class ConferirEstoqueController {
  
  private final ProdutoService produtoService;
  
  @GetMapping
  public ModelAndView pageIndex() {
    ModelAndView modelAndView = new ModelAndView("estoque/ConferirEstoque");
    modelAndView.addObject("listProdutos", produtoService.listProdutosConferirEstoque());
    return modelAndView;
  }
  
}

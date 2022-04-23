

package com.store.drinks.controller;

import com.store.drinks.entidade.enuns.TipoPagamento;
import com.store.drinks.service.AbrirCaixaService;
import com.store.drinks.service.ProdutoService;
import com.store.drinks.service.VendaService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/vendas")
@RequiredArgsConstructor
public class VendaController {
  
  private final AbrirCaixaService abrirCaixaService;
  private final VendaService vendaService;
  private final ProdutoService produtoService;
  
  @GetMapping
  public ModelAndView pageIndex() {
    if (!abrirCaixaService.abrirCaixaPorUsuarioLogado()) {
      return new ModelAndView("redirect:/pdv/abrirCaixa");
    }
    return new ModelAndView("venda/RealizarVenda");
  }
  
  @GetMapping("/produtos")
  public ResponseEntity<?> pesquisarProdutosAutoComplete(
    @RequestParam(name = "q", required = false) String descricao,
    @RequestParam(name = "page", defaultValue = "0", required = true) String page) {
    return new ResponseEntity<>(produtoService.pesquisarProdutosAutoComplete(descricao, page), HttpStatus.OK);
  }
  
  @PutMapping("/update-quantidade/{codigoBarra}/{quantidade}")
  @ResponseStatus(HttpStatus.OK)
  public void updateQuantidadeProdutoEstoque(@PathVariable(name = "codigoBarra", required = true) String codigoBarra, @PathVariable(name = "quantidade", required = true) Integer quantidade) {
    produtoService.updateQuantidadeProdutoEstoque(codigoBarra, quantidade);
  }
  
}

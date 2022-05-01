package com.store.drinks.controller;

import com.store.drinks.entidade.Caixa;
import com.store.drinks.entidade.Venda;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.service.CaixaService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pdv")
@PreAuthorize("hasRole('MANTER_PDV')")
public class CaixaController {

  @Autowired
  private CaixaService abrirCaixaService;

  @GetMapping
  public ModelAndView pageIndex() {
    if (!abrirCaixaService.abrirCaixaPorUsuarioLogado()) {
      return new ModelAndView("redirect:/pdv/abrirCaixa");
    }
    return new ModelAndView("venda/RealizarVenda");
  }

  @GetMapping("/abrirCaixa")
  public ModelAndView abrirCaixa(Caixa caixa) {
    return new ModelAndView("venda/AbrirCaixa");
  }

  @GetMapping("/vendas")
  public ModelAndView vendas(Venda venda) {
    if (!abrirCaixaService.abrirCaixaPorUsuarioLogado()) {
      return new ModelAndView("redirect:/pdv/abrirCaixa");
    }
    return new ModelAndView("venda/RealizarVenda");
  }

  @PostMapping("/salvar")
  public ModelAndView salvar(@Valid Caixa caixa, BindingResult result, Model model, RedirectAttributes attributes) {
    try {
      if (result.hasErrors()) {
        return new ModelAndView("rediredct:/pdv/abrirCaixa");
      }
      abrirCaixaService.salvar(caixa);
    } catch (NegocioException ex) {
      ObjectError error = new ObjectError("erro", ex.getMessage());
      result.addError(error);
    }
    attributes.addFlashAttribute("mensagem", "Caixa aberto com sucesso!");
    return new ModelAndView("redirect:/pdv/vendas");
  }
}
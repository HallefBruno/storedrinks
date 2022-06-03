package com.store.drinks.controller;

import com.store.drinks.entidade.Caixa;
import com.store.drinks.entidade.Venda;
import com.store.drinks.entidade.dto.usuario.UsuarioMovimentacaoCaixadto;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.service.CaixaService;
import com.store.drinks.service.MovimentacaoCaixaService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/caixa")
@PreAuthorize("hasRole('MANTER_PDV')")
@RequiredArgsConstructor
public class CaixaController {

  private final CaixaService abrirCaixaService;
  private final MovimentacaoCaixaService movimentacaoCaixaService;

  @GetMapping
  public ModelAndView pageIndex() {
    if (!abrirCaixaService.abrirCaixaPorUsuarioLogado()) {
      return new ModelAndView("redirect:/caixa/abrirCaixa");
    }
    return new ModelAndView("venda/RealizarVenda");
  }

  @GetMapping("/abrirCaixa")
  public ModelAndView abrirCaixa(Caixa caixa) {
    return new ModelAndView("caixa/AbrirCaixa");
  }

  @GetMapping("/vendas")
  public ModelAndView vendas(Venda venda) {
    if (!abrirCaixaService.abrirCaixaPorUsuarioLogado()) {
      return new ModelAndView("redirect:/caixa/abrirCaixa");
    }
    return new ModelAndView("venda/RealizarVenda");
  }

  @PostMapping("/salvar")
  public ModelAndView salvar(@Valid Caixa caixa, BindingResult result, Model model, RedirectAttributes attributes) {
    try {
      if (result.hasErrors()) {
        return abrirCaixa(caixa);
      }
      abrirCaixaService.salvar(caixa);
    } catch (NegocioException ex) {
      ObjectError error = new ObjectError("erro", ex.getMessage());
      result.addError(error);
    }
    attributes.addFlashAttribute("mensagem", "Caixa aberto com sucesso!");
    return new ModelAndView("redirect:/caixa/abrirCaixa");
  }
  
  @GetMapping("/fechar/{id}")
  public ModelAndView fechar(@PathVariable Long id,Caixa caixa, BindingResult result, RedirectAttributes attributes) {
    try {
      abrirCaixaService.fecharCaixa(id);
    } catch (NegocioException ex) {
      ObjectError error = new ObjectError("erro", ex.getMessage());
      result.addError(error);
    }
    attributes.addFlashAttribute("mensagem", "Caixa fechado com sucesso!");
    return new ModelAndView("redirect:/caixa/detalhes");
  }
  
  @GetMapping("/detalhes")
  public ModelAndView pageFecharCaixa(Caixa caixa, Long id, BindingResult result) {
    ModelAndView andView = new ModelAndView("caixa/FecharCaixa");
    try {
      setModewAndViewForPageFecharCaixa(andView, id);
      return andView;
    } catch (ResponseStatusException ex) {
      ObjectError error = new ObjectError("erro", ex.getReason());
      result.addError(error);
      try {
        setModewAndViewForPageFecharCaixa(andView, null);
      } catch (ResponseStatusException e) {
      }
      return andView;
    }
  }
  
  @GetMapping("/por-usuario/{id}")
  public ModelAndView porUsuario(@PathVariable Long id, Caixa caixa, BindingResult result) {
    ModelAndView andView = pageFecharCaixa(caixa, id, result);
    return andView;
  }
  
  @GetMapping("/usuarios")
  @PreAuthorize("hasRole('FILTRAR_POR_USUARIO_MOVIMENTACAO_CAIXA')")
  public ResponseEntity<List<UsuarioMovimentacaoCaixadto>> getUsuarios() {
    var list = movimentacaoCaixaService.getUsuarios();
    return ResponseEntity.ok(list);
  }
  
  private void setModewAndViewForPageFecharCaixa(ModelAndView modelAndView, Long id) {
    modelAndView.addObject("cxAbertoPorUsuario", abrirCaixaService.getCaixa(id));
    modelAndView.addObject("valorTotalEmVendasPorUsuario", abrirCaixaService.valorTotalEmVendasPorUsuario());
  }
}

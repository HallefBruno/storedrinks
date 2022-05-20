package com.store.drinks.controller;

import com.store.drinks.entidade.dto.usuario.UsuarioMovimentacaoCaixadto;
import com.store.drinks.repository.querys.movimentacaoCaixa.MovimentacoesCaixaFilters;
import com.store.drinks.service.MovimentacaoCaixaService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/movimentacao-caixa")
@RequiredArgsConstructor
public class MovimentacaoCaixaController {
  
  private final MovimentacaoCaixaService movimentacaoCaixaService;
  
  @GetMapping
  public ModelAndView pageIndex() {
    return new ModelAndView("movimentacaocaixa/MovimentacaoCaixa");
  }
  
  @GetMapping("/movimentacoes")
  public ResponseEntity<?> movimentacoes(
    @RequestParam(name = "draw", required = false) Integer draw,
    @RequestParam(name = "start", required = false) Integer start,
    @RequestParam(name = "length", required = false) Integer length,
    @RequestParam(name = "isCaixaFechado", required = true) Boolean isCaixaFechado,
    @RequestParam(name = "movimentacoesCaixaFilters", required = false) String movimentacoesCaixaFilters)
  {
    System.out.println(draw);
    System.out.println(start);
    System.out.println(length);
    System.out.println(isCaixaFechado);
    System.out.println(movimentacoesCaixaFilters);
    movimentacaoCaixaService.movimentacoesCaixa(movimentacoesCaixaFilters, draw, start);
    return null;//return movimentacaoCaixaService.movimentacoesCaixa(new MovimentacoesCaixaFilters(), 0, 0);
  }
  
  @GetMapping("/usuarios")
  @PreAuthorize("hasRole('FILTRAR_POR_USUARIO_MOVIMENTACAO_CAIXA')")
  public ResponseEntity<List<UsuarioMovimentacaoCaixadto>> getUsuarios() {
    var list = movimentacaoCaixaService.getUsuarios();
    return ResponseEntity.ok(list);
  }
}

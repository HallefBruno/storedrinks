package com.store.drinks.controller;

import com.store.drinks.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class DashBoardController {
  
  private final DashBoardService boardService;
  
  @PreAuthorize("hasRole('DASHBOARD')")
  @GetMapping("/")
  public ModelAndView pageIndex() {
    return new ModelAndView("Dashboard");
  }
  
  @PreAuthorize("hasRole('DASHBOARD')")
  @GetMapping("dashboard/pesquisar")
  public ResponseEntity<?> pesqisar(@RequestParam String filters) {
    return ResponseEntity.ok(boardService.produtosMaisVendidos(filters));
  }
  
  @PreAuthorize("hasRole('DASHBOARD')")
  @GetMapping("dashboard/vendas-tempo-real")
  public ResponseEntity<?> vendasTempoReal(
    @RequestParam(name = "draw", required = false) Integer draw,
    @RequestParam(name = "start", required = false) Integer start,
    @RequestParam(name = "length", required = false) Integer length,
    @RequestParam(name = "usuarioId", required = false) Long usuarioId) {
    var list = boardService.listVendasTempoReal(usuarioId, draw, start, length);
    return ResponseEntity.ok(list);
  }
  
  @PreAuthorize("hasRole('DASHBOARD')")
  @GetMapping("dashboard/detalhe-produto-vendido/{idVenda}")
  public ResponseEntity<?> listDetalheProdutoVendido(@PathVariable(required = true) Long idVenda) {
    var list = boardService.listDetalheProdutoVendido(idVenda);
    return ResponseEntity.ok(list);
  }
  
  @PreAuthorize("hasRole('DASHBOARD')")
  @GetMapping("dashboard/total-vendas")
  public ResponseEntity<?> totalVenda(@RequestParam(name = "idUsuario", required = false) Long idUsuario) {
    return ResponseEntity.ok(boardService.totalVenda(idUsuario));
  }
  
  @PreAuthorize("hasRole('DASHBOARD')")
  @GetMapping("dashboard/total-custo")
  public ResponseEntity<?> totalCusto(@RequestParam(name = "idUsuario", required = false) Long idUsuario) {
    return ResponseEntity.ok(boardService.totalCusto(idUsuario));
  }
  
  @GetMapping("/dash-2")
  public ModelAndView dash2() {
    return new ModelAndView("Dashboard2");
  }
}

package com.store.drinks.controller;

import com.store.drinks.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = {"/", "/dashboard"})
@RequiredArgsConstructor
public class DashBoardController {
  
  private final DashBoardService boardService;
  
  @GetMapping
  public ModelAndView pageIndex() {
    return new ModelAndView("Dashboard");
  }
  
  @GetMapping("/pesquisar")
  public ResponseEntity<?> pesqisar(@RequestParam String filters) {
    return ResponseEntity.ok(boardService.produtosMaisVendidos(filters));
  }
  
  @GetMapping("/vendas-tempo-real")
  public ResponseEntity<?> vendasTempoReal(
    @RequestParam(name = "draw", required = false) Integer draw,
    @RequestParam(name = "start", required = false) Integer start,
    @RequestParam(name = "length", required = false) Integer length,
    @RequestParam(name = "usuarioId", required = false) Long usuarioId) {
    var list = boardService.listVendasTempoReal(usuarioId, draw, start, length);
    return ResponseEntity.ok(list);
  }
 
}

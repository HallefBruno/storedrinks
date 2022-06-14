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
@RequestMapping("/")
@RequiredArgsConstructor
public class DashBoardController {
  
  private final DashBoardService boardService;
  
  @GetMapping
  public ModelAndView pageIndex() {
    return new ModelAndView("Dashboard");
  }
  
  @GetMapping("/dashBoard/pesquisar")
  public ResponseEntity<?> pesqisar(@RequestParam String filters) {
    return ResponseEntity.ok(boardService.produtosMaisVendidos(filters));
  }
 
}

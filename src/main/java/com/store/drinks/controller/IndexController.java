package com.store.drinks.controller;

import com.store.drinks.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class IndexController {
  
  private final DashBoardService boardService;
  
  @GetMapping
  public ModelAndView pageIndex() {
    boardService.teste();
    return new ModelAndView("Dashboard");
  }

}

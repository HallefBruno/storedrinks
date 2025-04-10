package com.store.drinks.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/congratulation")
@PreAuthorize("hasRole('INTEGRACOES')")
public class CongratulationController extends EncerrarSessaoController {
  
  @GetMapping
  public ModelAndView page(HttpServletRequest request, HttpServletResponse response) {
    return new ModelAndView("congratulation/Congratulation");
  }
  
  @GetMapping("/login")
  public void login(HttpServletRequest request, HttpServletResponse response) {
    encerrarSessao(request, response);
  }
  
}

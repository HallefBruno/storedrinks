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
public class CongratulationController {
  
  @GetMapping
  public ModelAndView page() {
    return new ModelAndView("congratulation/Congratulation");
  }
  
  @GetMapping("/login")
  public String login(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession(false);
    SecurityContextHolder.clearContext();
    if (session != null) {
      session.invalidate();
    }
    for (Cookie cookie : request.getCookies()) {
      cookie.setMaxAge(0);
    }
    return "Login";
  }
  
}

package com.store.drinks.controller;

import com.store.drinks.service.UsuarioService;
import java.util.Objects;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SegurancaController {
  
  @Autowired
  private UsuarioService usuarioService;
  
  @GetMapping("/login")
  public String login(@AuthenticationPrincipal User user) {
    if (Objects.nonNull(user)) {
      return "redirect:/";
    }
    return "Login";
  }

  @GetMapping("/403")
  public String acessoNegado() {
    return "403";
  }
  
  @GetMapping("/check-access")
  public String checkAccess(HttpServletRequest request, HttpServletResponse response) {
    if(!usuarioService.usuarioAtivo()) {
      HttpSession session = request.getSession(false);
      SecurityContextHolder.clearContext();
      if (session != null) {
        session.invalidate();
      }
      for (Cookie cookie : request.getCookies()) {
        cookie.setMaxAge(0);
      }
      return "/login?invalid-session=true";
    }
    return null;
  }
}

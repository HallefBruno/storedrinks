package com.store.drinks.controller;

import com.store.drinks.entidade.Usuario;
import com.store.drinks.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("userteste")
public class UsuariosTesteController {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @GetMapping
  public ModelAndView pageIndex(Usuario usuario) {
    ModelAndView mv = new ModelAndView("UsuariosTeste");
    mv.addObject("usuarios", usuarioRepository.findAll());
    return mv;
  }

}

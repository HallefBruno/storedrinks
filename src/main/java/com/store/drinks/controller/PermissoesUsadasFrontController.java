

package com.store.drinks.controller;

import com.store.drinks.entidade.PermissaoUsadaFront;
import com.store.drinks.repository.PermissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/permissoes-usadas-front")
@PreAuthorize("hasRole('SUPER_USER')")
@RequiredArgsConstructor
public class PermissoesUsadasFrontController {
  
  private final PermissaoRepository permissaoRepository;
  
  @GetMapping
  public ModelAndView pageIndex(PermissaoUsadaFront permissaoUsadaFront) {
    ModelAndView view = new ModelAndView("permissoesfront/Nova");
    view.addObject("todasPermissoes", permissaoRepository.findAll());
    return view;
  }
  
}

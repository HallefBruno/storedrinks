package com.store.drinks.service;

import com.store.drinks.entidade.enuns.Tenant;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.repository.UsuarioRepository;
import com.store.drinks.security.UsuarioSistema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  public Usuario usuarioLogado() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Usuario usuario = ((UsuarioSistema) authentication.getPrincipal()).getUsuario();
    return usuario;
  }

}

package com.store.drinks.service;

import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.dto.Usuariodto;
import com.store.drinks.repository.UsuarioRepository;
import com.store.drinks.security.UsuarioSistema;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;

  public Usuario usuarioLogado() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Usuario usuario = ((UsuarioSistema) authentication.getPrincipal()).getUsuario();
    return usuario;
  }
  
  public Optional<Usuario> findByEmailAndAtivoTrue(String email) {
    return usuarioRepository.findByEmailAndAtivoTrue(email);
  }

  public List<Usuariodto> buscarUsuariosPorTenant() {
    var usuarioLogado = usuarioLogado();
    var filtroUsuariosPorTenant = usuarioRepository.findAllByAtivoTrueAndClienteSistemaTenantAndEmailNotLike(usuarioLogado.getClienteSistema().getTenant(), usuarioLogado.getEmail());
    var usuariodtos = new ArrayList<Usuariodto>();
    filtroUsuariosPorTenant.forEach(usuario -> {
      var usuariodto = new Usuariodto();
      usuariodto.setId(usuario.getId());
      usuariodto.setText(usuarioLogado.getClienteSistema().getTenant());
      usuariodto.setNome(usuarioLogado.getClienteSistema().getNomeComercio());
      usuariodtos.add(usuariodto);
    });
    return usuariodtos;
  }
}

package com.store.drinks.service;

import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.dto.Usuariodto;
import com.store.drinks.entidade.wrapper.Select2Wrapper;
import com.store.drinks.repository.UsuarioRepository;
import com.store.drinks.security.UsuarioSistema;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
  
  public List<Usuariodto> buscarUsuariosPorTenant() {
    var usuarioLogado = usuarioLogado();
    var filtroUsuariosPorTenant = usuarioRepository.findAllByAtivoTrueAndClienteSistemaTenantAndEmailNotLike(usuarioLogado.getClienteSistema().getTenant(), usuarioLogado.getEmail());
    var usuariodtos = new ArrayList<Usuariodto>();
    filtroUsuariosPorTenant.forEach(usuario -> {
      var usuariodto = Usuariodto.builder().build();
      usuariodto.setId(usuario.getId().toString());
      usuariodto.setEmail(usuario.getEmail());
      usuariodto.setText(usuarioLogado.getClienteSistema().getTenant());
      usuariodto.setComercio(usuarioLogado.getClienteSistema().getNomeComercio());
      usuariodtos.add(usuariodto);
    });
    return usuariodtos;
  }
  
  public Select2Wrapper<Usuariodto> pesquisarComercioAutoComplete(String descricao, String pagina) {
    Pageable pageable = PageRequest.of(Integer.valueOf(pagina), 10);
    var pagePesquisarComercio = usuarioRepository.pesquisarComercioAutoComplete(descricao, pageable);
    var select2Wrapper = new Select2Wrapper<Usuariodto>();
    List<Usuariodto> usuariodtos = new ArrayList<>();
    select2Wrapper.setTotalItens(pagePesquisarComercio.getTotalElements());
    pagePesquisarComercio.getContent().forEach(comercio -> {
      var usuariodto = Usuariodto.builder().build();
      usuariodto.setId(comercio.getId().toString());
      usuariodto.setEmail(comercio.getEmail());
      usuariodto.setText(comercio.getClienteSistema().getTenant());
      usuariodto.setComercio(comercio.getClienteSistema().getNomeComercio());
      usuariodtos.add(usuariodto);
    });
    select2Wrapper.setItems(usuariodtos);
    return select2Wrapper;
  }
}

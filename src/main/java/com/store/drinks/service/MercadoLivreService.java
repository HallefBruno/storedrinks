package com.store.drinks.service;

import com.store.drinks.entidade.MercadoLivreCode;
import com.store.drinks.repository.MercadoLivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MercadoLivreService {
  
  @Autowired
  private MercadoLivreRepository mercadoLivreRepository;
  
  //@PreAuthorize("hasRole('SUPER_USER')")
  @Transactional
  public void salvar(String code) {
    var usuario = UsuarioService.usuarioLogado();
    if(usuario.getProprietario()) {
      var mercadoLivreCode = new MercadoLivreCode();
      mercadoLivreCode.setCode(code);
      mercadoLivreCode.setUsuario(UsuarioService.usuarioLogado());
      mercadoLivreRepository.save(mercadoLivreCode);
    }
  }
}

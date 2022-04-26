
package com.store.drinks.service;

import com.store.drinks.repository.util.Multitenancy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VendaService {
  
  private final Multitenancy multitenancy;
  private final UsuarioService usuarioService;
  private final AbrirCaixaService abrirCaixaService;
  
  @Transactional
  public void salvar() {
    
  }
  
}

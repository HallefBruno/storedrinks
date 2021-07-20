
package com.store.drinks.service;

import com.store.drinks.repository.AberturaCaixaRepository;
import com.store.drinks.security.UsuarioSistema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AberturaCaixaService {
    
    @Autowired
    private AberturaCaixaRepository aberturaCaixaRepository;
    
    public boolean caixaAberto() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsuarioSistema usuarioSistema = (UsuarioSistema) auth.getPrincipal();
        aberturaCaixaRepository.findByUsuario(usuarioSistema.getUsuario());
        return true;
    }
    
}

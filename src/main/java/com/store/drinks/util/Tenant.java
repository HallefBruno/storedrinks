
package com.store.drinks.util;

import com.store.drinks.entidade.Usuario;
import com.store.drinks.security.UsuarioSistema;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class Tenant {
    
    public Usuario usuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken) && authentication != null) {
            Usuario usuario = ((UsuarioSistema) authentication.getPrincipal()).getUsuario();
            return usuario;
        }
        return new Usuario();
    }
    
}

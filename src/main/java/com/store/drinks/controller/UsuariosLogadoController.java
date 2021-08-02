package com.store.drinks.controller;

import com.store.drinks.security.UsuarioSistema;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuariosLogadoController {
    
    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping("/loggedUsers")
    public String getLoggedUsers(Locale locale, Model model) {
        model.addAttribute("users", findAllLoggedInUsers());
        return "onlines/UsuariosLogado";
    }
    
    public List<UsuarioSistema> findAllLoggedInUsers() {
        return sessionRegistry.getAllPrincipals()
            .stream()
            .filter(principal -> principal instanceof UsuarioSistema)
            .map(UsuarioSistema.class::cast)
            .collect(Collectors.toList());
    }
}

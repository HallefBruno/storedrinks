
package com.store.drinks.controller;

import com.store.drinks.execption.NegocioException;
import com.store.drinks.service.ValidarClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("validar")
public class ValidarClienteSistema {
    
    @Autowired
    private ValidarClienteService validarClienteService;
    
    @GetMapping
    public String index() {
        return "public/ValidarClienteSistema";
    }
    
    @GetMapping("cliente")
    public @ResponseBody ResponseEntity<?> clienteCadastrado(@RequestParam(name = "cpfCnpj") String cpfCnpj) {
        try {
            validarClienteService.salvar(cpfCnpj);
            return ResponseEntity.ok("");
        } catch (NegocioException ex) {
            return ResponseEntity.badRequest().body(ex);
        }
    }
    
}

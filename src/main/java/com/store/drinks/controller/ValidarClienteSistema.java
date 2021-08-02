
package com.store.drinks.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("validar")
public class ValidarClienteSistema {
    
    @GetMapping
    public String index() {
        return "public/ValidarClienteSistema";
    }
    
    @GetMapping("cliente")
    public @ResponseBody ResponseEntity<?> clienteCadastrado(@RequestParam(name = "cpfCnpj") String cpfCnpj) {
        return ResponseEntity.ok(cpfCnpj);
    }
    
}

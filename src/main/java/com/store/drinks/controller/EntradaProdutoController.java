
package com.store.drinks.controller;

import com.store.drinks.entidade.EntradaProduto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("entradas")
public class EntradaProdutoController {
    
    @GetMapping
    public ModelAndView index(EntradaProduto entradaProduto) {
        return new ModelAndView("entradaproduto/EntradaProduto");
    }
    
    @GetMapping("novo")
    public ModelAndView novo(EntradaProduto entradaProduto) {
        return new ModelAndView("entradaproduto/EntradaProduto");
    }
    
}

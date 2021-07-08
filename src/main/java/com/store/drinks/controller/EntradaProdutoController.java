
package com.store.drinks.controller;

import com.store.drinks.entidade.EntradaProduto;
import com.store.drinks.service.EntradaProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("entradas")
public class EntradaProdutoController {
    
    @Autowired
    private EntradaProdutoService entradaProdutoService;
    
    @GetMapping
    public ModelAndView index(EntradaProduto entradaProduto) {
        ModelAndView mv = new ModelAndView("entradaproduto/EntradaProduto");
        mv.addObject("produtos", entradaProdutoService.todos());
        return mv;
    }
    
    @GetMapping("novo")
    public ModelAndView novo(EntradaProduto entradaProduto) {
        ModelAndView mv = new ModelAndView("entradaproduto/EntradaProduto");
        mv.addObject("produtos", entradaProdutoService.todos());
        return mv;
    }
    
}

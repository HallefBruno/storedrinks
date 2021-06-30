
package com.store.drinks.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("produtos")
public class ProdutoController {
    
    @RequestMapping
    public ModelAndView init() {
        ModelAndView mv = new ModelAndView("produto/Produto");
        return mv;
    }
    
    @RequestMapping(path = "pesquisar")
    public ModelAndView pesqisar() {
        ModelAndView mv = new ModelAndView("produto/Pesquisar");
        return mv;
    }
    
}

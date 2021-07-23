
package com.store.drinks.controller;

import com.store.drinks.entidade.MovimentacaoCaixa;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("pdv")
public class PdvController {
    
    
    
    @GetMapping
    public ModelAndView pagina() {
        
        return new ModelAndView("redirect:/pdv/abrirCaixa");
        
        //return new ModelAndView("Dashboard");
    }
    
    @GetMapping("abrirCaixa")
    public ModelAndView abrirCaixa(MovimentacaoCaixa movimentacaoCaixa) {
        return new ModelAndView("venda/MovimentacaoCaixa");
    }
    
    @PostMapping("teste")
    public void teste() {
        System.out.println("ok");
    }
    
}

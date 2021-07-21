
package com.store.drinks.controller;

import com.store.drinks.service.AberturaCaixaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("pdv")
public class PdvController {
    
    @Autowired
    private AberturaCaixaService aberturaCaixaService;
    
    @GetMapping
    public ModelAndView pagina() {
        if(aberturaCaixaService.caixaAberto()) {
            return new ModelAndView("venda/MovimentacaoCaixa");
        }
        return new ModelAndView("Dashboard");
    }
    
}


package com.store.drinks.controller;

import com.store.drinks.entidade.Produto;
import com.store.drinks.service.ProdutoService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("produtos")
public class ProdutoController {
    
    @Autowired
    private ProdutoService produtoService;
    
    @RequestMapping
    public ModelAndView init(Produto produto) {
        ModelAndView mv = new ModelAndView("produto/Novo");
        return mv;
    }
    
    @PostMapping("salvar")
    public ModelAndView salvar(@Valid Produto produto, BindingResult result, Model model, RedirectAttributes attributes ) {
        try {
            if (result.hasErrors()) {
                return init(produto);
            }
            produtoService.salvar(produto);
        } catch (Exception ex) {
            ObjectError error = new ObjectError("erro", "Esse produto ja foi cadastrado");
            result.addError(error);
            return init(produto);
        }
        attributes.addFlashAttribute("mensagem", "Produto salvo com sucesso!");
        return new ModelAndView("redirect:/produtos", HttpStatus.CREATED);
    }
    
    @RequestMapping(path = "pesquisar")
    public ModelAndView pesqisar() {
        ModelAndView mv = new ModelAndView("produto/Pesquisar");
        return mv;
    }
    
}


package com.store.drinks.controller;

import com.store.drinks.entidade.EntradaProduto;
import com.store.drinks.entidade.Produto;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.service.EntradaProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        return mv;
    }
    
    @GetMapping("novo")
    public ModelAndView novo(EntradaProduto entradaProduto) {
        ModelAndView mv = new ModelAndView("entradaproduto/EntradaProduto");
        return mv;
    }
    
    @GetMapping("buscar/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable(required = true, name = "id") Long id) {
        try {
            return ResponseEntity.ok(entradaProdutoService.buscarProdutoPorId(id));
        } catch (NegocioException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("buscar/produtoPorCodBarra/{codBarra}")
    public ResponseEntity<Produto> buscarProdutoPorCodBarra(@PathVariable(required = true, name = "codBarra") String codBarra) {
        try {
            return ResponseEntity.ok(entradaProdutoService.buscarProdutoPorCodBarra(codBarra));
        } catch (NegocioException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("produtos")
    public ResponseEntity<?> pesquisarProdutosAutoComplete(
            @RequestParam(name = "q", required = false) String descricao,
            @RequestParam(name = "page",defaultValue = "0", required = true) String page) {
        return new ResponseEntity<>(entradaProdutoService.pesquisarProdutosAutoComplete(descricao, page),HttpStatus.OK);
    }
    
}

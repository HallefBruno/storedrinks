
package com.store.drinks.controller;

import com.store.drinks.entidade.EntradaProduto;
import com.store.drinks.entidade.Produto;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.service.EntradaProdutoService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    
    @GetMapping("nova")
    public ModelAndView novo(EntradaProduto entradaProduto) {
        ModelAndView mv = new ModelAndView("entradaproduto/EntradaProduto");
        return mv;
    }
    
    @PostMapping("salvar")
    public ModelAndView salvar(@Valid EntradaProduto entradaProduto, BindingResult result, Model model, RedirectAttributes attributes) {
        try {
            if (result.hasErrors()) {
                return novo(entradaProduto);
            }
            this.entradaProdutoService.salvar(entradaProduto);
        } catch (NegocioException ex) {
            ObjectError error = new ObjectError("erro", ex.getMessage());
            result.addError(error);
            return novo(entradaProduto);
        }
        attributes.addFlashAttribute("mensagem", "Entrada salvo com sucesso!");
        return new ModelAndView("redirect:/entradas/novo", HttpStatus.CREATED);
    }
    
    @GetMapping("buscar/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable(required = true, name = "id") Long id) {
        try {
            return ResponseEntity.ok(entradaProdutoService.buscarProdutoPorId(id));
        } catch (NegocioException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("alterarSituacao/{id}")
    public ModelAndView alterarSituacaoEntrada(@PathVariable(required = true, name = "id") Long id, RedirectAttributes attributes) {
        entradaProdutoService.alterarSituacaoEntrada(id);
        attributes.addFlashAttribute("mensagem", "Situação da entrada alterada com sucesso!");
        return new ModelAndView("redirect:/entradas/pesquisar", HttpStatus.OK);
    }
    
    @GetMapping("buscar/produtoPorCodBarra/{codBarra}")
    public ResponseEntity<Produto> buscarProdutoPorCodBarra(@PathVariable(required = true, name = "codBarra") String codBarra) {
        try {
            return ResponseEntity.ok(entradaProdutoService.buscarProdutoPorCodBarra(codBarra));
        } catch (NegocioException ex) {
            return ResponseEntity.noContent().build();
        }
    }
    
    @GetMapping("produtos")
    public ResponseEntity<?> pesquisarProdutosAutoComplete(
            @RequestParam(name = "q", required = false) String descricao,
            @RequestParam(name = "page",defaultValue = "0", required = true) String page) {
        return new ResponseEntity<>(entradaProdutoService.pesquisarProdutosAutoComplete(descricao, page),HttpStatus.OK);
    }
    
    @GetMapping("pesquisar")
    public ModelAndView pesqisar() {
        ModelAndView mv = new ModelAndView("entradaproduto/Pesquisar");
        mv.addObject("pagina", entradaProdutoService.todas());
        return mv;
    }
    
}

package com.store.drinks.controller;

import com.store.drinks.controller.page.PageWrapper;
import com.store.drinks.entidade.Produto;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.repository.ProdutoRepository;
import com.store.drinks.repository.querys.produto.ProdutoFilter;
import com.store.drinks.service.ProdutoService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("produtos")
public class ProdutoController {

  private final ProdutoService produtoService;
  private final ProdutoRepository produtoRepository;

  @Autowired
  public ProdutoController(ProdutoService produtoService, ProdutoRepository produtoRepository) {
    this.produtoService = produtoService;
    this.produtoRepository = produtoRepository;
  }

  @RequestMapping
  public ModelAndView pageIndex(Produto produto) {
    ModelAndView mv = new ModelAndView("produto/Novo");
    return mv;
  }

  @RequestMapping("novo")
  public ModelAndView pageNovo(Produto produto) {
    ModelAndView mv = new ModelAndView("produto/Novo");
    return mv;
  }

  @PreAuthorize("hasRole('MANTER_PRODUTO')")
  @PostMapping("salvar")
  public ModelAndView salvar(@Valid Produto produto, BindingResult result, Model model, RedirectAttributes attributes) {
    try {
      if (result.hasErrors()) {
        return pageNovo(produto);
      }
      produtoService.salvar(produto);
    } catch (NegocioException ex) {
      ObjectError error = new ObjectError("erro", ex.getMessage());
      result.addError(error);
      return pageNovo(produto);
    }
    attributes.addFlashAttribute("mensagem", "Produto salvo com sucesso!");
    return new ModelAndView("redirect:/produtos/novo", HttpStatus.CREATED);
  }

  @PreAuthorize("hasRole('MANTER_PRODUTO')")
  @PostMapping("update/{codigo}")
  public ModelAndView update(@PathVariable(required = true, name = "codigo") Long codigo, @Valid Produto produto, BindingResult result, RedirectAttributes attributes) {
    try {
      if (result.hasErrors()) {
        return pageNovo(produto);
      }
      produtoService.update(produto, codigo);
    } catch (NegocioException ex) {
      ObjectError error = new ObjectError("erro", ex.getMessage());
      result.addError(error);
      return pageNovo(produto);
    }
    attributes.addFlashAttribute("mensagem", "Produto alterado com sucesso!");
    return new ModelAndView("redirect:/produtos/novo", HttpStatus.OK);
  }

  @GetMapping("pesquisar")
  public ModelAndView pesqisar(ProdutoFilter produtoFilter, BindingResult result, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest httpServletRequest) {
    ModelAndView mv = new ModelAndView("produto/Pesquisar");
    PageWrapper<Produto> paginaWrapper = new PageWrapper<>(produtoRepository.filtrar(produtoFilter, pageable), httpServletRequest);
    mv.addObject("pagina", paginaWrapper);
    return mv;
  }

  @PreAuthorize("hasRole('MANTER_PRODUTO')")
  @DeleteMapping("{codigo}")
  public ResponseEntity<?> excluir(@PathVariable("codigo") Produto produto) {
    try {
      produtoService.excluir(produto);
    } catch (NegocioException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    return ResponseEntity.ok().build();
  }

  @GetMapping("{codigo}")
  public ModelAndView editar(@PathVariable("codigo") Produto produto) {
    ModelAndView mv = pageNovo(produto);
    mv.addObject(produto);
    return mv;
  }

}

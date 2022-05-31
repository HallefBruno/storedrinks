package com.store.drinks.controller;

import com.store.drinks.controller.page.PageWrapper;
import com.store.drinks.entidade.EntradaProduto;
import com.store.drinks.entidade.Produto;
import com.store.drinks.entidade.enuns.TipoPagamento;
import com.store.drinks.entidade.enuns.SituacaoCompra;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.repository.EntradaProdutoRepository;
import com.store.drinks.repository.querys.entrada.EntradasFilter;
import com.store.drinks.service.EntradaProdutoService;
import com.store.drinks.service.FornecedorService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/entradas")
@PreAuthorize("hasRole('MANTER_ENTRADA')")
@RequiredArgsConstructor
public class EntradaProdutoController {

  private final EntradaProdutoService entradaProdutoService;
  private final EntradaProdutoRepository entradaProdutoRepository;
  private final FornecedorService fornecedorService;
  
  @GetMapping
  public ModelAndView pageNova(EntradaProduto entradaProduto) {
    ModelAndView mv = new ModelAndView("entradaproduto/EntradaProduto");
    mv.addObject("fornecedores", fornecedorService.todos());
    mv.addObject("formasPagamento", TipoPagamento.values());
    mv.addObject("situacoesCompra", SituacaoCompra.values());
    return mv;
  }
  
  @PostMapping
  public ResponseEntity<EntradaProduto> salvar(@Valid @RequestBody(required = true) EntradaProduto entradaProduto) {
    entradaProdutoService.salvar(entradaProduto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/buscar/{id}")
  public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable(required = true, name = "id") Long id) {
    try {
      return ResponseEntity.ok(entradaProdutoService.buscarProdutoPorId(id));
    } catch (NegocioException ex) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/alterarSituacao/{id}")
  public ModelAndView alterarSituacaoEntrada(@PathVariable(required = true, name = "id") Long id, RedirectAttributes attributes) {
    entradaProdutoService.alterarSituacaoEntrada(id);
    attributes.addFlashAttribute("mensagem", "Situação da entrada alterada com sucesso!");
    return new ModelAndView("redirect:/entradas/pesquisar");
  }

  @GetMapping("/buscar/produtoPorCodBarra/{codBarra}")
  public ResponseEntity<Produto> buscarProdutoPorCodBarra(@PathVariable(required = true, name = "codBarra") String codBarra) {
    try {
      return ResponseEntity.ok(entradaProdutoService.buscarProdutoPorCodBarra(codBarra));
    } catch (NegocioException ex) {
      return ResponseEntity.noContent().build();
    }
  }

  @GetMapping("/produtos")
  public ResponseEntity<?> pesquisarProdutosAutoComplete(
    @RequestParam(name = "q", required = false) String descricao,
    @RequestParam(name = "page", defaultValue = "0", required = true) String page) {
    return new ResponseEntity<>(entradaProdutoService.pesquisarProdutosAutoComplete(descricao, page), HttpStatus.OK);
  }
  
  @GetMapping("/pesquisar")
  public ModelAndView pesqisar(EntradasFilter entradasFilter, BindingResult result, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest httpServletRequest) {
    ModelAndView mv = new ModelAndView("entradaproduto/Pesquisar");
    PageWrapper<EntradaProduto> paginaWrapper = new PageWrapper<>(entradaProdutoRepository.filtrar(entradasFilter, pageable), httpServletRequest);
    mv.addObject("pagina", paginaWrapper);
    mv.addObject("fornecedores", fornecedorService.todos());
    return mv;
  }

}
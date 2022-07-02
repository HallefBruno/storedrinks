package com.store.drinks.controller;

import com.store.drinks.entidade.PermissaoUsadaFront;
import com.store.drinks.repository.PermissaoRepository;
import com.store.drinks.service.PermissaoUsadaFrontService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/permissoes-usadas-front")
@PreAuthorize("hasRole('SUPER_USER')")
@RequiredArgsConstructor
public class PermissoesUsadasFrontController {
  
  private final PermissaoRepository permissaoRepository;
  private final PermissaoUsadaFrontService permissaoUsadaFrontService;
  
  @GetMapping
  public ModelAndView pageNovo(PermissaoUsadaFront permissaoUsadaFront) {
    ModelAndView view = new ModelAndView("permissoesfront/Nova");
    view.addObject("todasPermissoes", permissaoRepository.findAll());
    return view;
  }
  
  @GetMapping("/listar")
  public ModelAndView listarTodos() {
    ModelAndView view = new ModelAndView("permissoesfront/Pesquisar");
    view.addObject("listaPermissoes", permissaoUsadaFrontService.todos());
    return view;
  }
  
  @PostMapping
  public ModelAndView salvar(@Valid PermissaoUsadaFront permissaoUsadaFront, BindingResult result, RedirectAttributes attributes) {
    try {
      if (result.hasErrors()) {
        return pageNovo(permissaoUsadaFront);
      }
      permissaoUsadaFrontService.salvar(permissaoUsadaFront);
    } catch (ResponseStatusException ex) {
      ObjectError error = new ObjectError("erro", ex.getReason());
      result.addError(error);
      return pageNovo(permissaoUsadaFront);
    }
    attributes.addFlashAttribute("mensagem", "Operação salva com sucesso!");
    return new ModelAndView("redirect:/permissoes-usadas-front");
  }
  
  @PostMapping("update/{codigo}")
  public ModelAndView update(@PathVariable(required = true, name = "codigo") Long codigo, @Valid PermissaoUsadaFront permissaoUsadaFront, BindingResult result, RedirectAttributes attributes) {
    try {
      if (result.hasErrors()) {
        return pageNovo(permissaoUsadaFront);
      }
      permissaoUsadaFrontService.update(permissaoUsadaFront, codigo);
    } catch (ResponseStatusException ex) {
      ObjectError error = new ObjectError("erro", ex.getReason());
      result.addError(error);
      return pageNovo(permissaoUsadaFront);
    }
    attributes.addFlashAttribute("mensagem", "Fornecedor alterado com sucesso!");
    return new ModelAndView("redirect:/fornecedor");
  }
  
   @DeleteMapping("{codigo}")
  public ResponseEntity<?> excluir(@PathVariable("codigo") PermissaoUsadaFront permissaoUsadaFront) {
    try {
      permissaoUsadaFrontService.excluir(permissaoUsadaFront);
    } catch (ResponseStatusException e) {
      return ResponseEntity.badRequest().body(e.getReason());
    }
    return ResponseEntity.ok().build();
  }
  
  @GetMapping("{codigo}")
  public ModelAndView editar(@PathVariable("codigo") PermissaoUsadaFront permissaoUsadaFront) {
    ModelAndView mv = pageNovo(permissaoUsadaFront);
    mv.addObject(permissaoUsadaFront);
    return mv;
  }
  
}

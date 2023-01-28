package com.store.drinks.controller;

import com.store.drinks.controller.page.PageWrapper;
import com.store.drinks.entidade.EntradaProduto;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.repository.filtros.EntradasFiltro;
import com.store.drinks.repository.filtros.UsuarioFiltro;
import com.store.drinks.service.GrupoService;
import com.store.drinks.service.UsuarioService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("hasRole('MANTER_USUARIO')")
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {
  
  private final GrupoService grupoService;
  private final UsuarioService usuarioService;
  
  @GetMapping
  public ModelAndView pageIndex(Usuario usuario) {
    ModelAndView modelAndView = new ModelAndView("usuario/Novo");
    modelAndView.addObject("grupos", grupoService.gruposParaUsuarioNovaConta());
    return modelAndView;
  }
  
  @PostMapping("/salvar")
  public ModelAndView salvar(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes, @RequestParam("image") MultipartFile image) throws IOException {
    try {
      if (result.hasErrors()) {
        return pageIndex(usuario);
      }
      usuarioService.salvar(usuario, image);
    } catch (ResponseStatusException ex) {
      ObjectError error = new ObjectError("erro", ex.getReason());
      result.addError(error);
      return pageIndex(usuario);
    }
    attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso!");
    return new ModelAndView("redirect:/usuario");
  }
  
  @PostMapping("/update/{codigo}")
  public ModelAndView update(@PathVariable(required = true, name = "codigo") Long codigo, @Valid Usuario usuario, BindingResult result, RedirectAttributes attributes, @RequestParam("image") MultipartFile image) {
    try {
      if (result.hasErrors()) {
        return pageIndex(usuario);
      }
      usuarioService.update(usuario, codigo, image);
    } catch (ResponseStatusException ex) {
      ObjectError error = new ObjectError("erro", ex.getReason());
      result.addError(error);
      return pageIndex(usuario);
    }
    attributes.addFlashAttribute("mensagem", "Usuário alterado com sucesso!");
    return new ModelAndView("redirect:/usuario");
  }
  
  @GetMapping("/pesquisar")
  public ModelAndView pesqisar(UsuarioFiltro usuarioFiltro, BindingResult result, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest httpServletRequest) {
    ModelAndView mv = new ModelAndView("usuario/Pesquisar");
    mv.addObject("usuarios", usuarioService.filtrar(usuarioFiltro));
    return mv;
  }
  
  @GetMapping("{codigo}")
  public ModelAndView editar(@PathVariable("codigo") Usuario usuario) {
    ModelAndView mv = pageIndex(usuario);
    mv.addObject(usuario);
    return mv;
  }
  
}

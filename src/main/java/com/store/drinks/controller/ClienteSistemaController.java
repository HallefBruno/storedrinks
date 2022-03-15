package com.store.drinks.controller;

import com.store.drinks.entidade.ClienteSistema;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.service.ClienteSistemaService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clienteSistema")
public class ClienteSistemaController {

  @Autowired
  private ClienteSistemaService clienteSistemaService;

  @PreAuthorize("hasRole('SUPER_USER')")
  @GetMapping("/novo")
  public String index(ClienteSistema clienteSistema, Model model) {
    model.addAttribute("clienteSistema", clienteSistema);
    return "clientesistema/Novo";
  }

  @PreAuthorize("hasRole('SUPER_USER')")
  @GetMapping
  public ModelAndView novo(ClienteSistema clienteSistema) {
    ModelAndView modelAndView = new ModelAndView("clientesistema/Novo");
    modelAndView.addObject("clienteSistema", clienteSistema);
    return modelAndView;
  }

  @PreAuthorize("hasRole('SUPER_USER')")
  @GetMapping("/pesquisar")
  public String pesquisar(ClienteSistema clienteSistema, Model model) {
    model.addAttribute("clienteSistema", clienteSistema);
    return "clientesistema/Pesquisar";
  }

  @PreAuthorize("hasRole('SUPER_USER')")
  @PostMapping("/salvar")
  public ModelAndView salvar(@Valid ClienteSistema clienteSistema, BindingResult result, Model model, RedirectAttributes attributes) {
    try {
      if (result.hasErrors()) {
        return novo(clienteSistema);
      }
      clienteSistemaService.salvar(clienteSistema);
    } catch (NegocioException ex) {
      ObjectError error = new ObjectError("erro", ex.getMessage());
      result.addError(error);
      return novo(clienteSistema);
    }
    attributes.addFlashAttribute("mensagem", "Novo cliente sistema cadastrado com sucesso!");
    return new ModelAndView("redirect:/clienteSistema", HttpStatus.CREATED);
  }

}

package com.store.drinks.controller;

import com.store.drinks.execption.NegocioException;
import com.store.drinks.service.NovaContaClienteSistema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ValidarClienteSistema {

  @Autowired
  private NovaContaClienteSistema validarClienteService;

  @RequestMapping(path = {"validar"}, method = RequestMethod.GET)
  public String pageValidarCliente() {
    return "novaconta/ValidarClienteSistema";
  }

  @GetMapping("validar/cliente")
  public @ResponseBody ResponseEntity<?> clienteCadastrado(@RequestParam(name = "cpfCnpj") String cpfCnpj) {
    try {
      validarClienteService.salvaValidarCliente(cpfCnpj);
      return ResponseEntity.ok("novaConta");
    } catch (NegocioException ex) {
      return ResponseEntity.badRequest().body(ex);
    }
  }

  @RequestMapping(path = {"novaConta"}, method = RequestMethod.GET)
  public String pageNovaConta() {
    return "novaconta/CriarContaClienteSistema";
  }

}

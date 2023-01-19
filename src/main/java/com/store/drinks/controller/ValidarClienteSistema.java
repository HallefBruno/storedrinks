package com.store.drinks.controller;

import com.store.drinks.execption.NegocioException;
import com.store.drinks.service.NovaContaClienteSistema;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/validar")
public class ValidarClienteSistema {

  @Autowired
  private NovaContaClienteSistema validarClienteService;
  
  @GetMapping
  public String pageValidarCliente() {
    return "novaconta/ValidarClienteSistema";
  }

  @GetMapping("/cliente")
  public @ResponseBody ResponseEntity<?> clienteCadastrado(@RequestParam(name = "cpfCnpj") String cpfCnpj) {
    try {
      validarClienteService.salvaValidarCliente(cpfCnpj);
      return ResponseEntity.ok("validar/nova-conta");
    } catch (NegocioException ex) {
      return ResponseEntity.badRequest().body(ex);
    }
  }
  
  @GetMapping("/cpfcnpj/{cpfCnpj}")
  public ResponseEntity<Boolean> validarCpfCnpj(@PathVariable(required = true) String cpfCnpj) {
    return ResponseEntity.ok(validarClienteService.verificarCpfCnpj(cpfCnpj));
  }
  
  @GetMapping("/nova-conta")
  public String pageNovaConta() {
    return "novaconta/CriarContaClienteSistema";
  }
  
  @GetMapping("/finalizar")
  @ResponseStatus(HttpStatus.OK)
  public void criarNovaConta(
    @RequestHeader(required = true, value="cpfcnpj") String cpfcnpj,
    @RequestHeader(required = true, value="nome") String nome,
    @RequestHeader(required = true, value="dataNascimento") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataNascimento,
    @RequestHeader(required = true, value="email") String email,
    @RequestHeader(required = true, value="password") String password
    ) 
  {
    validarClienteService.criarNovaContaCliente(cpfcnpj, nome, dataNascimento, email, password);
  }

}


package com.store.drinks.controller;

import com.store.drinks.entidade.Mensagem;
import com.store.drinks.service.MensagemService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/mensagens")
public class MensagemController {
  
  @Autowired
  private MensagemService mensagemService;

  @GetMapping
  public ModelAndView pageIndex() {
    return new ModelAndView("mensagem/Recebidas");
  }
  
  @GetMapping("/pesquisar")
  public ResponseEntity<List<Mensagem>> pesquisar(Boolean lida, BindingResult result, @PageableDefault(size = 10) Pageable pageable) {
    return ResponseEntity.ok(mensagemService.findAllByLida(lida, pageable));
  }
  
  @PreAuthorize("hasRole('ENVIAR_MENSAGEM')")
  @PostMapping
  public void enviarMensagem(@RequestBody(required = true) @Valid Mensagem mensagem) {
    mensagemService.salvarMensagem(mensagem);
  }
  
}

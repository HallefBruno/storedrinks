
package com.store.drinks.controller;

import com.store.drinks.entidade.Mensagem;
import com.store.drinks.entidade.dto.Usuariodto;
import com.store.drinks.service.MensagemService;
import com.store.drinks.service.UsuarioService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/mensagens")
@RequiredArgsConstructor
public class MensagemController {
  
  private final MensagemService mensagemService;
  private final UsuarioService usuarioService;

  @GetMapping("/recebidas")
  public ModelAndView pageIndex() {
    return new ModelAndView("mensagem/Recebidas");
  }
  
  @GetMapping("/nova")
  public ModelAndView pageNovaMensagem() {
    return new ModelAndView("mensagem/Nova"); 
  }
  
  @GetMapping("/usuarios")
  public ResponseEntity<List<Usuariodto>> listaUsuarioPorTenant() {
    var listaUsuarios = usuarioService.buscarUsuariosPorTenant();
    if(!ObjectUtils.isEmpty(listaUsuarios)) {
      return ResponseEntity.ok(listaUsuarios);
    }
    return ResponseEntity.noContent().build();
  }
  
  @GetMapping("/destinatario")
  public ResponseEntity<?> pesquisarProdutosAutoComplete(
    @RequestParam(name = "q", required = false) String descricao,
    @RequestParam(name = "page", defaultValue = "0", required = true) String page) {
    return new ResponseEntity<>(usuarioService.pesquisarComercioAutoComplete(descricao, page), HttpStatus.OK);
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

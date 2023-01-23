package com.store.drinks.controller;

import com.store.drinks.entidade.MensagemRecebida;
import com.store.drinks.entidade.dto.Mensagemdto;
import com.store.drinks.entidade.dto.usuario.UsuarioMensagemdto;
import com.store.drinks.entidade.wrapper.DataTableWrapper;
import com.store.drinks.service.MensagemRecebidaService;
import com.store.drinks.service.UsuarioService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/mensagens")
@RequiredArgsConstructor
public class MensagemRecebidaController {
  
  private final MensagemRecebidaService mensagensRecebidasService;
  private final UsuarioService usuarioService;
  
  @PreAuthorize("hasRole('LER_MENSAGEM')")
  @GetMapping("/recebidas")
  public ModelAndView pageIndex() {
    return new ModelAndView("mensagem/Recebidas");
  }
  
  @PreAuthorize("hasRole('ENVIAR_MENSAGEM')")
  @GetMapping("/nova")
  public ModelAndView pageNovaMensagem() {
    return new ModelAndView("mensagem/Nova"); 
  }
  
  @PreAuthorize("hasRole('ENVIAR_MENSAGEM')")
  @GetMapping("/usuarios")
  public ResponseEntity<List<UsuarioMensagemdto>> listaUsuarioPorTenant() {
    var listaUsuarios = usuarioService.buscarUsuariosPorTenant();
    if(!ObjectUtils.isEmpty(listaUsuarios)) {
      return ResponseEntity.ok(listaUsuarios);
    }
    return ResponseEntity.noContent().build();
  }
  
  @PreAuthorize("hasRole('ENVIAR_MENSAGEM')")
  @GetMapping("/destinatario")
  public ResponseEntity<?> pesquisarProdutosAutoComplete(
    @RequestParam(name = "q", required = false) String descricao,
    @RequestParam(name = "page", defaultValue = "0", required = true) String page) {
    return new ResponseEntity<>(mensagensRecebidasService.pesquisarComercioAutoComplete(descricao, page), HttpStatus.OK);
  }
  
  @PreAuthorize("hasRole('LER_MENSAGEM')")
  @GetMapping("/pesquisar/{lida}")
  public ResponseEntity<DataTableWrapper<MensagemRecebida>> pesquisar(@PathVariable(required = true) Boolean lida, 
    @RequestParam(name = "draw", required = false) Integer draw, 
    @RequestParam(name = "start", required = false) Integer start,  
    @RequestParam(name = "length", required = false) Integer length) {
    return ResponseEntity.ok(mensagensRecebidasService.findAllByLida(lida, draw, start, length));
  }
  
  @PreAuthorize("hasRole('LER_MENSAGEM')")
  @GetMapping("/nao-lidas")
  public ResponseEntity<Void> existeMensagemNaoLida() {
    Boolean isMensagensNaoLidas = mensagensRecebidasService.existeMensagemNaoLida();
    return isMensagensNaoLidas ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
  }
  
  @PreAuthorize("hasRole('ENVIAR_MENSAGEM')")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void enviarMensagem(@RequestBody(required = true) @Valid Mensagemdto mensagem) {
    mensagensRecebidasService.salvarMensagemEnviadaRecebida(mensagem);
  }
  
  @PreAuthorize("hasRole('LER_MENSAGEM')")
  @PutMapping("/notificado")
  @ResponseStatus(HttpStatus.OK)
  public void marcarComoNotificado() {
    mensagensRecebidasService.marcarComoNotificado();
  }
  
  @PreAuthorize("hasRole('LER_MENSAGEM')")
  @PutMapping("/marcar-como-lida/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void marcarComoLida(@PathVariable(required = true) Long id) {
    mensagensRecebidasService.marcarComoLida(id);
  }
  
}

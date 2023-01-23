package com.store.drinks.service;

import com.store.drinks.entidade.MensagemEnviada;
import com.store.drinks.entidade.MensagemRecebida;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.dto.Mensagemdto;
import com.store.drinks.entidade.dto.usuario.UsuarioMensagemdto;
import com.store.drinks.entidade.embedded.RemetenteDestinatarioMensagem;
import com.store.drinks.entidade.wrapper.DataTableWrapper;
import com.store.drinks.entidade.wrapper.Select2Wrapper;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.store.drinks.repository.MensagemRecebidaRepository;

@Service
@RequiredArgsConstructor
public class MensagemRecebidaService {
  private final UsuarioService usuarioService;
  
  private final MensagemEnviadaService mensagensEnviadasService;
  private final MensagemRecebidaRepository mensagensRecebidasRepository;
  
  @Transactional
  public void salvarMensagemEnviadaRecebida(Mensagemdto mensagem) {
    Usuario usuario = UsuarioService.usuarioLogado();
    Usuario usuarioDestino = usuarioService.findByEmailAndAtivoTrue(mensagem.getDestinatario()).get();
    MensagemEnviada mensagensEnviadas = new MensagemEnviada();
    RemetenteDestinatarioMensagem remetenteDestinatarioMensagem = new RemetenteDestinatarioMensagem();
    remetenteDestinatarioMensagem.setDestinatario(mensagem.getDestinatario());
    remetenteDestinatarioMensagem.setMensagem(mensagem.getMensagem());
    remetenteDestinatarioMensagem.setRemetente(usuario.getEmail());
    mensagensEnviadas.setDataHoraMensagemEnviada(LocalDateTime.now());
    mensagensEnviadas.setRemetenteDestinatarioMensagem(remetenteDestinatarioMensagem);
    mensagensEnviadas.setUsuario(usuarioDestino);
    
    MensagemRecebida mensagensRecebidas = new MensagemRecebida();
    mensagensRecebidas.setDataHoraMensagemRecebida(mensagensEnviadas.getDataHoraMensagemEnviada());
    mensagensRecebidas.setLida(Boolean.FALSE);
    mensagensRecebidas.setNotificado(Boolean.FALSE);
    mensagensRecebidas.setRemetenteDestinatarioMensagem(remetenteDestinatarioMensagem);
    mensagensRecebidas.setUsuario(usuario);
    
    mensagensEnviadasService.salvar(mensagensEnviadas);
    salvar(mensagensRecebidas);
  }
  
  @Transactional
  public void salvar(MensagemRecebida mensagensRecebidas) {
    mensagensRecebidasRepository.save(mensagensRecebidas);
  }

  public int marcarComoNotificado() {
    String destinatario = UsuarioService.usuarioLogado().getEmail();
    return mensagensRecebidasRepository.updateNotificarMensagem(destinatario);
  }
  
  public Boolean existeMensagemNaoLida() {
    String destinatario = UsuarioService.usuarioLogado().getEmail();
    return mensagensRecebidasRepository.existeMensagemNaoLida(destinatario);
  }
  
  @Transactional
  public void marcarComoLida(Long id) {
    if(Objects.isNull(id) || id <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identifacador inválido!");
    }
    String destinatario = UsuarioService.usuarioLogado().getEmail();
    mensagensRecebidasRepository.findByIdAndRemetenteDestinatarioMensagemDestinatario(id, destinatario).map(mensagensRecebidas -> {
      mensagensRecebidas.setLida(Boolean.TRUE);
      mensagensRecebidas.setNotificado(Boolean.TRUE);
      mensagensRecebidasRepository.save(mensagensRecebidas);
      return Void.TYPE;
    });
  }
  
  public DataTableWrapper<MensagemRecebida> findAllByLida(Boolean lida, int draw, int start, int length) {
    String email = UsuarioService.usuarioLogado().getEmail();
    int page = start/length;
    Pageable pageable = PageRequest.of(page,length);
    DataTableWrapper<MensagemRecebida> dataTable = new DataTableWrapper<>();
    Page<MensagemRecebida> pageMensagens = mensagensRecebidasRepository.findByLidaAndRemetenteDestinatarioMensagemDestinatario(lida, email, pageable);
    dataTable.setData(pageMensagens.getContent());
    dataTable.setDraw(draw);
    dataTable.setStart(start);
    dataTable.setRecordsTotal(pageMensagens.getTotalElements());
    dataTable.setRecordsFiltered(pageMensagens.getTotalElements());
    return dataTable;
  }
  
  public Select2Wrapper<UsuarioMensagemdto> pesquisarComercioAutoComplete(String descricao, String pagina) {
    Pageable pageable = PageRequest.of(Integer.parseInt(pagina), 10);
    var pagePesquisarComercio = mensagensRecebidasRepository.pesquisarComercioAutoComplete(descricao, pageable);
    var select2Wrapper = new Select2Wrapper<UsuarioMensagemdto>();
    select2Wrapper.setTotalItens(pagePesquisarComercio.getTotalElements());
    select2Wrapper.setItems(pagePesquisarComercio.getContent());
    return select2Wrapper;
  }
}

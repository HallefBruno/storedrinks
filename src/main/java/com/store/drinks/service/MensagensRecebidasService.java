package com.store.drinks.service;

import com.store.drinks.entidade.MensagensEnviadas;
import com.store.drinks.entidade.MensagensRecebidas;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.dto.Mensagemdto;
import com.store.drinks.entidade.dto.usuario.UsuarioMensagemdto;
import com.store.drinks.entidade.embedded.RemetenteDestinatarioMensagem;
import com.store.drinks.entidade.wrapper.DataTableWrapper;
import com.store.drinks.entidade.wrapper.Select2Wrapper;
import com.store.drinks.repository.MensagensEnviadasRepository;
import com.store.drinks.repository.MensagensRecebidasRepository;
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

@Service
@RequiredArgsConstructor
public class MensagensRecebidasService {
  private final UsuarioService usuarioService;
  
  private final MensagensEnviadasRepository mensagensEnviadasRepository;
  private final MensagensRecebidasRepository mensagensRecebidasRepository;
  
  @Transactional
  public void salvarMensagemEnviadaRecebida(Mensagemdto mensagem) {
    
    Usuario usuario = usuarioService.usuarioLogado();
    Usuario usuarioDestino = usuarioService.findByEmailAndAtivoTrue(mensagem.getDestinatario()).get();
    MensagensEnviadas mensagensEnviadas = new MensagensEnviadas();
    RemetenteDestinatarioMensagem remetenteDestinatarioMensagem = new RemetenteDestinatarioMensagem();
    remetenteDestinatarioMensagem.setDestinatario(mensagem.getDestinatario());
    remetenteDestinatarioMensagem.setMensagem(mensagem.getMensagem());
    remetenteDestinatarioMensagem.setRemetente(usuario.getEmail());
    mensagensEnviadas.setDataHoraMensagemEnviada(LocalDateTime.now());
    mensagensEnviadas.setRemetenteDestinatarioMensagem(remetenteDestinatarioMensagem);
    mensagensEnviadas.setUsuario(usuarioDestino);
    
    MensagensRecebidas mensagensRecebidas = new MensagensRecebidas();
    mensagensRecebidas.setDataHoraMensagemRecebida(mensagensEnviadas.getDataHoraMensagemEnviada());
    mensagensRecebidas.setLida(Boolean.FALSE);
    mensagensRecebidas.setNotificado(Boolean.FALSE);
    mensagensRecebidas.setRemetenteDestinatarioMensagem(remetenteDestinatarioMensagem);
    mensagensRecebidas.setUsuario(usuario);
    
    mensagensEnviadasRepository.save(mensagensEnviadas);
    mensagensRecebidasRepository.save(mensagensRecebidas);
  }

  public int marcarComoNotificado() {
    String destinatario = usuarioService.usuarioLogado().getEmail();
    return mensagensRecebidasRepository.updateNotificarMensagem(destinatario);
  }
  
  public Boolean existemMensagensNaoLidas() {
    String destinatario = usuarioService.usuarioLogado().getEmail();
    return mensagensRecebidasRepository.existemMensagensNaoLidas(destinatario);
  }
  
  @Transactional
  public void marcarComoLida(Long id) {
    if(Objects.isNull(id) || id <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identifacador inválido!");
    }
    String destinatario = usuarioService.usuarioLogado().getEmail();
    mensagensRecebidasRepository.findByIdAndRemetenteDestinatarioMensagemDestinatario(id, destinatario).map(mensagensRecebidas -> {
      mensagensRecebidas.setLida(Boolean.TRUE);
      mensagensRecebidas.setNotificado(Boolean.TRUE);
      mensagensRecebidasRepository.save(mensagensRecebidas);
      return Void.TYPE;
    });
  }
  
  public DataTableWrapper<MensagensRecebidas> findAllByLida(Boolean lida, int draw, int start, int length) {
    String email = usuarioService.usuarioLogado().getEmail();
    int page = start/length;
    Pageable pageable = PageRequest.of(page,length);
    DataTableWrapper<MensagensRecebidas> dataTable = new DataTableWrapper<>();
    Page<MensagensRecebidas> pageMensagens = mensagensRecebidasRepository.findByLidaAndRemetenteDestinatarioMensagemDestinatario(lida, email, pageable);
    dataTable.setData(pageMensagens.getContent());
    dataTable.setDraw(draw);
    dataTable.setStart(start);
    dataTable.setRecordsTotal(pageMensagens.getTotalElements());
    dataTable.setRecordsFiltered(pageMensagens.getTotalElements());
    return dataTable;
  }
  
  public Select2Wrapper<UsuarioMensagemdto> pesquisarComercioAutoComplete(String descricao, String pagina) {
    Pageable pageable = PageRequest.of(Integer.valueOf(pagina), 10);
    var pagePesquisarComercio = mensagensRecebidasRepository.pesquisarComercioAutoComplete(descricao, pageable);
    var select2Wrapper = new Select2Wrapper<UsuarioMensagemdto>();
    select2Wrapper.setTotalItens(pagePesquisarComercio.getTotalElements());
    select2Wrapper.setItems(pagePesquisarComercio.getContent());
    return select2Wrapper;
  }
}

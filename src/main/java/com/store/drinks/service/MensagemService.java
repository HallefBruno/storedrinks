
package com.store.drinks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.drinks.entidade.Mensagem;
import com.store.drinks.entidade.dto.Usuariodto;
import com.store.drinks.entidade.dto.mensagem.Mensagemdto;
import com.store.drinks.entidade.wrapper.DataTable;
import com.store.drinks.entidade.wrapper.Select2Wrapper;
import com.store.drinks.repository.MensagemRepository;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MensagemService {
  
  private final MensagemRepository mensagemRepository;
  private final UsuarioService usuarioService;
  
  @Transactional
  @PreAuthorize("hasRole('ENVIAR_MENSAGEM')")
  public void salvarMensagem(Mensagem mensagem) {
    mensagem.setNotificado(Boolean.FALSE);
    mensagem.setDataHoraMensagemRecebida(LocalDateTime.now());
    mensagemRepository.save(mensagem);
  }
  
  public int marcarComoNotificado() {
    String tenant = usuarioService.usuarioLogado().getClienteSistema().getTenant();
    Long usuarioId = usuarioService.usuarioLogado().getId();
    return mensagemRepository.updateNotificarMensagem(tenant, usuarioId);
  }
  
  public Boolean existemMensagensNaoLidas() {
    String tenant = usuarioService.usuarioLogado().getClienteSistema().getTenant();
    Long usuarioId = usuarioService.usuarioLogado().getId();
    return mensagemRepository.existemMensagensNaoLidas(tenant, usuarioId);
  }
  
  public DataTable<Mensagemdto> findAllByLida(Boolean lida, int draw, int start, int length) {
    String email = usuarioService.usuarioLogado().getEmail();
    int page = start/length;
    Pageable pageable = PageRequest.of(page,length);
    DataTable<Mensagemdto> dataTable = new DataTable<>();
    Page<Mensagemdto> pageMensagens = mensagemRepository.findAllByLida(lida,email, pageable);
    dataTable.setData(pageMensagens.getContent());
    dataTable.setDraw(draw);
    dataTable.setStart(start);
    dataTable.setRecordsTotal(pageMensagens.getTotalElements());
    dataTable.setRecordsFiltered(pageMensagens.getTotalElements());
    //return mensagemRepository.findAllByLida(lida, pageable);
    return dataTable;
  }
  
  public Select2Wrapper<Usuariodto> pesquisarComercioAutoComplete(String descricao, String pagina) {
    Pageable pageable = PageRequest.of(Integer.valueOf(pagina), 10);
    var pagePesquisarComercio = mensagemRepository.pesquisarComercioAutoComplete(descricao, pageable);
    var select2Wrapper = new Select2Wrapper<Usuariodto>();
    select2Wrapper.setTotalItens(pagePesquisarComercio.getTotalElements());
    select2Wrapper.setItems(pagePesquisarComercio.getContent());
    return select2Wrapper;
  }
}

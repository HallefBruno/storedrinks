
package com.store.drinks.service;

import com.store.drinks.entidade.Mensagem;
import com.store.drinks.entidade.dto.Usuariodto;
import com.store.drinks.entidade.wrapper.Select2Wrapper;
import com.store.drinks.repository.MensagemRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MensagemService {
  
  private final MensagemRepository mensagemRepository;
  
  @Transactional
  @PreAuthorize("hasRole('ENVIAR_MENSAGEM')")
  public void salvarMensagem(Mensagem mensagem) {
    mensagem.setDataHoraMensagemRecebida(LocalDateTime.now());
    mensagemRepository.save(mensagem);
  }
  
  public List<Mensagem> findAllByLida(Boolean lida, Pageable pageable) {
    return mensagemRepository.findAllByLida(lida, pageable);
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

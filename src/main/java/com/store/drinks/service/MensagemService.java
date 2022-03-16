
package com.store.drinks.service;

import com.store.drinks.entidade.Mensagem;
import com.store.drinks.repository.MensagemRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MensagemService {
  
  private final MensagemRepository mensagemRepository;
  
  @Transactional
  public void salvarMensagem(Mensagem mensagem) {
    mensagem.setLida(Boolean.FALSE);
    mensagem.setDataHoraMensagemRecebida(LocalDateTime.now());
    mensagemRepository.save(mensagem);
  }
  
  public List<Mensagem> findAllByLida(Boolean lida, Pageable pageable) {
    return mensagemRepository.findAllByLida(lida, pageable);
  }
  
}

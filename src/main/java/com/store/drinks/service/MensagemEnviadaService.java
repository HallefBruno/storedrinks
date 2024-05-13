package com.store.drinks.service;

import com.store.drinks.entidade.MensagemEnviada;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.store.drinks.repository.MensagemEnviadaRepository;

@Service
@RequiredArgsConstructor
public class MensagemEnviadaService {
  
  private final MensagemEnviadaRepository mensagensEnviadasRepository;
  
  @Transactional
  public void salvar(MensagemEnviada mensagensEnviadas) {
    mensagensEnviadasRepository.save(mensagensEnviadas);
  }
  
}

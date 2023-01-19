package com.store.drinks.service;

import com.store.drinks.entidade.MensagensEnviadas;
import com.store.drinks.repository.MensagensEnviadasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MensagensEnviadasService {
  
  private final MensagensEnviadasRepository mensagensEnviadasRepository;
  
  @Transactional
  public void salvar(MensagensEnviadas mensagensEnviadas) {
    mensagensEnviadasRepository.save(mensagensEnviadas);
  }
  
}

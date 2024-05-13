

package com.store.drinks.service;

import com.store.drinks.entidade.FormaPagamento;
import com.store.drinks.repository.FormaPagamentoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FormaPagamentoService {
  
  private final FormaPagamentoRepository formaPagamentoRepository;
  
  @Transactional
  public void salvar(FormaPagamento formaPagamento) {
    formaPagamentoRepository.save(formaPagamento);
  }
  
  @Transactional
  public void salvar(List<FormaPagamento> formasPagamento) {
    formaPagamentoRepository.saveAll(formasPagamento);
  }
  
}

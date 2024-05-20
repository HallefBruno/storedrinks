package com.store.drinks.service;

import com.store.drinks.entidade.PlataformasEcommerce;
import com.store.drinks.repository.PlataformasIntegracaoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PlataformasIntegracaoService {
  
  @Autowired
  private PlataformasIntegracaoRepository plataformasIntegracaoRepository;
  
  @Transactional
  public void update(PlataformasEcommerce plataformasIntegracao) {
    plataformasIntegracaoRepository.save(plataformasIntegracao);
  }
  
  public PlataformasEcommerce getByDescricao(String descricao) {
    return plataformasIntegracaoRepository.findByDescricaoIgnoreCase(descricao).get();
  }
  
  public List<PlataformasEcommerce> todasAtivas() {
    return plataformasIntegracaoRepository.findAll(Example.of(PlataformasEcommerce.of(Boolean.TRUE)), Sort.by(Sort.Direction.ASC, "id"));
  }
  
}

package com.store.drinks.service;

import com.store.drinks.entidade.Permissao;
import com.store.drinks.repository.PermissaoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissaoService {

  private final PermissaoRepository permissaoRepository;

  public List<Permissao> todas() {
    return permissaoRepository.findAll(Sort.unsorted().descending());
  }

}

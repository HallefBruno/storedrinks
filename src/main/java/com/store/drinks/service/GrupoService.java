package com.store.drinks.service;

import com.store.drinks.entidade.Grupo;
import com.store.drinks.entidade.enuns.GrupoEnum;
import com.store.drinks.repository.GrupoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class GrupoService {

  @Autowired
  private GrupoRepository grupoRepository;

  public List<Grupo> todos() {
    return grupoRepository.findAll(Sort.unsorted().ascending());
  }
  
  public List<Grupo> gruposParaUsuarioNovaConta() {
    List<Grupo> grupos = grupoRepository.findAll(Sort.unsorted().ascending());
    grupos.removeIf(grupo -> grupo.getNome().equalsIgnoreCase(GrupoEnum.SUPER_USER.getValue()));
    return grupos;
  }
}

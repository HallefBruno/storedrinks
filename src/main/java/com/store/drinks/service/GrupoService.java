package com.store.drinks.service;

import com.store.drinks.entidade.Grupo;
import com.store.drinks.entidade.enuns.GrupoEnum;
import com.store.drinks.repository.GrupoRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class GrupoService {
  
  private final String tenantSu = "sud";
  
  @Autowired
  private GrupoRepository grupoRepository;

  public List<Grupo> todos() {
    return grupoRepository.findAll(Sort.unsorted().ascending());
  }
  
  public List<Grupo> gruposParaUsuarioNovaConta() {
    List<Grupo> grupos = new ArrayList<>(UsuarioService.usuarioLogado().getGrupos());
    if(tenantSu.equalsIgnoreCase(UsuarioService.usuarioLogado().getClienteSistema().getTenant()) && UsuarioService.usuarioLogado().getProprietario()) {
      return todos();
    }
    grupos.removeIf(grupo -> grupo.getNome().equalsIgnoreCase(GrupoEnum.SUPER_USER.getValue()));
    if(grupos.size() == 1 && GrupoEnum.OPERADOR.getValue().equalsIgnoreCase(grupos.get(0).getNome())) {
      grupos.removeIf(grupo -> grupo.getNome().equalsIgnoreCase(GrupoEnum.ADMIN.getValue()));
    }
    return grupos;
  }
}

package com.store.drinks.repository.querys.usuario;

import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.dto.Usuariodto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioRepositoryCustom {
  Optional<Usuario> porEmailEAtivo(String email);
  List<String> permissoes(Usuario usuario);
  Page<Usuariodto> pesquisarComercioAutoComplete(String comercio, Pageable pageable);
}

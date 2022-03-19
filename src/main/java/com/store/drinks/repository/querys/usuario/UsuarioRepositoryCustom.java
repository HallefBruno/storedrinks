package com.store.drinks.repository.querys.usuario;

import com.store.drinks.entidade.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryCustom {
  Optional<Usuario> porEmailEAtivo(String email);
  List<String> permissoes(Usuario usuario);
  
}

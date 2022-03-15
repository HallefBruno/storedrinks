package com.store.drinks.repository.querys.usuario;

import com.store.drinks.entidade.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryCustom {

  public Optional<Usuario> porEmailEAtivo(String email);

  public List<String> permissoes(Usuario usuario);
}

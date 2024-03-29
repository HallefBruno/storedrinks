package com.store.drinks.repository.querys.usuario;

import com.store.drinks.entidade.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryCustom {
  Boolean existeEmail(String email);
  Boolean existeTelefone(String telefone);
  Boolean permiteCriarUsuario();
  Optional<Usuario> findByUserLogin(String email);
  List<String> permissoes(Usuario usuario);
}

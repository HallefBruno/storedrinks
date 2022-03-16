
package com.store.drinks.repository;

import com.store.drinks.entidade.Usuario;
import com.store.drinks.repository.querys.usuario.UsuarioRepositoryCustom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioRepositoryCustom {
  Optional<Usuario> findByEmail(String email);
  List<Usuario> findByAllAtivoTrueAndTenantAndEmailNotLike(String tenant, String email);
}

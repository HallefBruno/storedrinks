
package com.store.drinks.repository;

import com.store.drinks.entidade.Usuario;
import com.store.drinks.repository.querys.usuario.UsuarioRepositoryCustom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioRepositoryCustom {
  
  @EntityGraph(attributePaths = {"clienteSistema"})
  Optional<Usuario> findByEmailAndAtivoTrue(String email);
  
  @EntityGraph(attributePaths = {"clienteSistema"})
  List<Usuario> findAllByAtivoTrueAndClienteSistemaTenantAndEmailNotLike(String tenant, String email);
  
  Optional<Usuario> findByClienteSistemaTenantAndId(String tenant, Long id);
  
}

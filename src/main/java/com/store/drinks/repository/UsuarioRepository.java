
package com.store.drinks.repository;

import com.store.drinks.entidade.Usuario;
import com.store.drinks.repository.querys.usuario.UsuarioRepositoryCustom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioRepositoryCustom {
  
  @EntityGraph(attributePaths = {"clienteSistema"})
  Optional<Usuario> findByEmailAndAtivoTrue(String email);
  
  @EntityGraph(attributePaths = {"clienteSistema"})
  List<Usuario> findAllByAtivoTrueAndClienteSistemaTenantAndEmailNotLike(String tenant, String email);
  
  @EntityGraph(attributePaths = {"clienteSistema"})
  Optional<Usuario> findByClienteSistemaTenantAndId(String tenant, Long id);
  
  @EntityGraph(attributePaths = {"clienteSistema"})
  Optional<Usuario> findByImagemAndClienteSistemaTenant(String imagem, String tenant);
  
  @Query(
   "select u from Usuario u join fetch u.clienteSistema cs " +
   "where 1=1 " +
   "and (?1 is null or (upper(u.nome) like concat(?1, '%'))) " +
   "and (?2 is null or (upper(u.email) like concat(?2, '%'))) " +
   "and (?3 is null or (cs.tenant = ?3)) "
  )
  List<Usuario> filtrar(String nome, String email, String tenant);
  
  @Override
  Boolean existeEmail(String email);
  
  @Override
  Boolean existeTelefone(String telefone);
  
  @Query(" SELECT usuario.ativo FROM Usuario usuario WHERE usuario.id = :id ")
  Boolean usuarioAtivo(@Param("id") Long id);
  
}

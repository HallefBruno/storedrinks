
package com.store.drinks.repository;

import com.store.drinks.entidade.AbrirCaixa;
import com.store.drinks.entidade.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbrirCaixaRepository extends JpaRepository<AbrirCaixa, Long> {
    Optional<AbrirCaixa> findByAbertoTrueAndUsuario(Usuario usuario);
}

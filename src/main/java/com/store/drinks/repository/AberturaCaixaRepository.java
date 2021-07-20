
package com.store.drinks.repository;

import com.store.drinks.entidade.AberturaCaixa;
import com.store.drinks.entidade.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AberturaCaixaRepository extends JpaRepository<AberturaCaixa, Long>{
    Optional<AberturaCaixa> findByUsuario(Usuario usuario);
}

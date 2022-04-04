
package com.store.drinks.repository;

import com.store.drinks.entidade.EntradaProduto;
import com.store.drinks.repository.querys.entrada.EntradaProdutoRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntradaProdutoRepository extends JpaRepository<EntradaProduto, Long>, EntradaProdutoRepositoryCustom {
  Optional<EntradaProduto> findByNumeroNota(String numeroNota);
}

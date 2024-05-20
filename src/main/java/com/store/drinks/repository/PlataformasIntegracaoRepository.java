
package com.store.drinks.repository;

import com.store.drinks.entidade.PlataformasEcommerce;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlataformasIntegracaoRepository extends JpaRepository<PlataformasEcommerce, Long> {

  Optional<PlataformasEcommerce> findByDescricaoIgnoreCase(String descricao);
}


package com.store.drinks.repository;

import com.store.drinks.entidade.MercadoLivre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MercadoLivreRepository extends JpaRepository<MercadoLivre, Long> {
  
}

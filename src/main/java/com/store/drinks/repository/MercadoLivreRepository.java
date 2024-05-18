
package com.store.drinks.repository;

import com.store.drinks.entidade.MercadoLivreCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MercadoLivreRepository extends JpaRepository<MercadoLivreCode, Long> {
  
}

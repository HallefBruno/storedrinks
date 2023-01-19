
package com.store.drinks.repository;

import com.store.drinks.entidade.PermissaoUsadaFront;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissaoUsadaFrontRepository extends JpaRepository<PermissaoUsadaFront, Long> {
  
}

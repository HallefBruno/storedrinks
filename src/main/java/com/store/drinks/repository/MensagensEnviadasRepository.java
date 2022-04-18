package com.store.drinks.repository;

import com.store.drinks.entidade.MensagensEnviadas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensagensEnviadasRepository extends JpaRepository<MensagensEnviadas, Long> {
  
}

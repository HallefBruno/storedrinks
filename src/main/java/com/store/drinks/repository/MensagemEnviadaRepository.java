package com.store.drinks.repository;

import com.store.drinks.entidade.MensagemEnviada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensagemEnviadaRepository extends JpaRepository<MensagemEnviada, Long> {
  
}

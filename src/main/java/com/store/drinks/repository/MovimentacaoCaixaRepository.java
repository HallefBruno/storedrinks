package com.store.drinks.repository;

import com.store.drinks.entidade.MovimentacaoCaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimentacaoCaixaRepository extends JpaRepository<MovimentacaoCaixa, Long> {
  
}

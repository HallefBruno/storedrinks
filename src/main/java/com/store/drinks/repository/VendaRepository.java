package com.store.drinks.repository;

import com.store.drinks.entidade.Venda;
import com.store.drinks.repository.querys.venda.VendaRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long>, VendaRepositoryCustom {
  
}

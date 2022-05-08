package com.store.drinks.repository;

import com.store.drinks.entidade.ItensVenda;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItensVendaRepository extends JpaRepository<ItensVenda, Long> {
  List<ItensVenda> findAllByVendaId(Long id);
}

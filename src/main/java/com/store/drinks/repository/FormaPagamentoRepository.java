package com.store.drinks.repository;

import com.store.drinks.entidade.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {
  List<FormaPagamento> findAllByMovimentacaoCaixaId(Long id);
}

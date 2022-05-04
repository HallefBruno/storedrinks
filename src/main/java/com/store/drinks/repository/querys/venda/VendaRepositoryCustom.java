package com.store.drinks.repository.querys.venda;

import com.store.drinks.entidade.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VendaRepositoryCustom {
  Page<Venda> buscarVendasParaCancelar(Pageable pageable);
}

package com.store.drinks.repository.querys.venda;

import com.store.drinks.entidade.dto.venda.CancelarVendadto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VendaRepositoryCustom {
  Page<CancelarVendadto> getVendasCancelar(Pageable pageable);
}

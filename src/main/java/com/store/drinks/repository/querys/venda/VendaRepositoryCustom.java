package com.store.drinks.repository.querys.venda;

import com.store.drinks.entidade.dto.venda.CancelarVendadto;
import com.store.drinks.entidade.dto.venda.ItensVendaCancelardto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VendaRepositoryCustom {
  Page<CancelarVendadto> getVendasCancelar(Pageable pageable);
  List<ItensVendaCancelardto> getItensVenda(Long vendaId);
}

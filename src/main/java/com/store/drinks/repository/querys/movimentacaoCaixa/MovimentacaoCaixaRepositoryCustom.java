package com.store.drinks.repository.querys.movimentacaoCaixa;

import com.store.drinks.entidade.MovimentacaoCaixa;
import com.store.drinks.entidade.wrapper.DataTableWrapper;
import java.math.BigDecimal;
import java.util.Optional;

public interface MovimentacaoCaixaRepositoryCustom {
  
  Optional<BigDecimal> valorTotalEmVendasPorUsuario();
  DataTableWrapper<MovimentacaoCaixa> movimentacoesCaixa(MovimentacoesCaixaFilters movimentacoesCaixaFilters, int draw, int start);
  
}

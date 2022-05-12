package com.store.drinks.repository.querys.movimentacaoCaixa;

import java.math.BigDecimal;
import java.util.Optional;

public interface MovimentacaoCaixaRepositoryCustom {
  
  Optional<BigDecimal> valorTotalEmVendasPorUsuario();
  
}

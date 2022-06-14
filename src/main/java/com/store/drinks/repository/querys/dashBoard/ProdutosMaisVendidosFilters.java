package com.store.drinks.repository.querys.dashBoard;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutosMaisVendidosFilters implements Serializable {

  private String dataInicial;
  private String dataFinal;

}

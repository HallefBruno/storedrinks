package com.store.drinks.repository.filtros;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutosMaisVendidosFiltro implements Serializable {

  private String dataInicial;
  private String dataFinal;
  private Long usuarioId;

}

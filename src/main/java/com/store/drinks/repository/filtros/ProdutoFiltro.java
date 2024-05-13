package com.store.drinks.repository.filtros;

import java.io.Serializable;
import lombok.Data;

@Data
public class ProdutoFiltro implements Serializable {
  private String codBarra;
  private String codProduto;
  private String descricao;
}

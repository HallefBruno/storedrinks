package com.store.drinks.entidade.dto.produtosMaisVendidos;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutosMaisVendidosdto implements Serializable {

  private String descricaoProduto;
  private Long quantidade;
}

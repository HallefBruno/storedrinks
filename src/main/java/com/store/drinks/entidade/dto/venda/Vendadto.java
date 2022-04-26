

package com.store.drinks.entidade.dto.venda;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vendadto {
  private List<ItensVendadto> itensVenda;
  private List<FormaPagamentodto> formasPagamento;
}


package com.store.drinks.entidade.wrapper;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor
@Builder
public class Select2Wrapper<T> {
  private Long totalItens;
  private List<T> items;
}

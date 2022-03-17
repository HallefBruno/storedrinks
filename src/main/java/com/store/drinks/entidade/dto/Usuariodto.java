
package com.store.drinks.entidade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuariodto {
  private String id;
  private String text;
  private String email;
  private String comercio;
}

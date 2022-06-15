package com.store.drinks.entidade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mensagemdto {
  
  private String tenant;
  private String destinatario;
  private String mensagem;

}

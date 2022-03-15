package com.store.drinks.entidade.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NovaContaDTO {

  private String cpfCnpj;
  private String email;
  private String senha;
  private String confirmarSenha;

}

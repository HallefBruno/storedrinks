package com.store.drinks.repository.filtros;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UsuarioFiltro implements Serializable {
  private String nome;
  private String email;
}

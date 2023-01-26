package com.store.drinks.repository.filtros;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FornecedorFiltro implements Serializable {

  private String nomeFornecedor;
  private String cpfCnpj;
  private String telefone;

}

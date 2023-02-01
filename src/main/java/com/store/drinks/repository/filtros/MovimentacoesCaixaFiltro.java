package com.store.drinks.repository.filtros;

import com.store.drinks.entidade.dto.UsuarioSelect2;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovimentacoesCaixaFiltro implements Serializable {

  private UsuarioSelect2 usuarioSelect2;
  private String dataAbertura;
  private String dataFechamento;
  private Boolean somenteCaixaAberto;

}

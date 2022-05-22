package com.store.drinks.repository.querys.movimentacaoCaixa;

import com.store.drinks.entidade.dto.UsuarioSelect2;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovimentacoesCaixaFilters implements Serializable {
  
  private UsuarioSelect2 usuarioSelect2;
  private LocalDate dataInicio;
  private LocalDate dataFim;
  private Boolean somenteCaixaAberto;
  
}

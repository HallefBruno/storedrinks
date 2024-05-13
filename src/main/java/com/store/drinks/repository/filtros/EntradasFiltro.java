package com.store.drinks.repository.filtros;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@AllArgsConstructor
public class EntradasFiltro implements Serializable  {

  private String numeroNota;
  private String fornecedor;
  private String codBarra;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate dataEmissao;

}

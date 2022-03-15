package com.store.drinks.repository.querys.entrada;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@AllArgsConstructor
public class EntradasFilter {

  private String numeroNota;
  private String fornecedor;
  private String codBarra;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date dataEmissao;

}

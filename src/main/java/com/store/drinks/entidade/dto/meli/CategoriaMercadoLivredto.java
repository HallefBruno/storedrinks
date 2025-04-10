package com.store.drinks.entidade.dto.meli;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;

@Data
public class CategoriaMercadoLivredto implements Serializable {

  private String id;
  
  @JsonProperty("name")
  private String nome;
}


package com.store.drinks.entidade.dto;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuariodto implements Serializable {
  
  @Id
  private Long id;
  private String text;
  private String nome;
  private String destinatario;
  private String tenant;
}

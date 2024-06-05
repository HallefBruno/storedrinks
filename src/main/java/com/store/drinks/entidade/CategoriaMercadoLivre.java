
package com.store.drinks.entidade;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class CategoriaMercadoLivre implements Serializable {

  @Id
  private String id;
  
  @Column(unique = true)
  private String nome;
  
}


package com.store.drinks.entidade.dto.mensagem;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MensagemUsuariodto implements Serializable {
  
  @Id
  private BigInteger id;
  
}

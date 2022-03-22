
package com.store.drinks.entidade.dto.mensagem;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@SqlResultSetMapping(
  name="Mensagemdto",
  entities={
    @EntityResult(entityClass=Mensagemdto.class),
    @EntityResult(entityClass=MensagemUsuariodto.class,
      fields={
        @FieldResult(name="id",column="usuario_id"),
      }
    )
  }
)
@AllArgsConstructor
@NoArgsConstructor
public class Mensagemdto implements Serializable {

  @Id
  private BigInteger id;
  private Boolean lida;
  private Boolean notificado;
  
}

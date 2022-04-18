
package com.store.drinks.entidade.dto.mensagem;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
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

@SqlResultSetMapping(name = "Mensagemdto",
  entities = {
    @EntityResult(entityClass = Mensagemdto.class, 
      fields = {
        @FieldResult(name = "id", column = "id"),
        @FieldResult(name = "tenant", column = "tenant"),
        @FieldResult(name = "destinatario", column = "destinatario"),
        @FieldResult(name = "mensagem", column = "mensagem"),
        @FieldResult(name = "lida", column = "lida"),
        @FieldResult(name = "dataHoraMensagemRecebida", column = "data_hora_mensagem_recebida"),
        @FieldResult(name = "usuarioId", column = "usuario_id"),
        @FieldResult(name = "notificado", column = "notificado"),
        @FieldResult(name = "remetente", column = "remetente")
      }
    )
  }
)
@AllArgsConstructor
@NoArgsConstructor
public class Mensagemdto implements Serializable {

  @Id
  private BigInteger id;
  private String tenant;
  private String destinatario;
  private String mensagem;
  private Boolean lida;
  private LocalDateTime dataHoraMensagemRecebida;
  private BigInteger usuarioId;
  private Boolean notificado;
  private String remetente;

}

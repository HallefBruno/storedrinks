

package com.store.drinks.entidade.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SqlResultSetMapping(name = "Caixadto",
  entities = {
    @EntityResult(entityClass = com.store.drinks.entidade.dto.Caixadto.class, 
      fields = {
        @FieldResult(name = "id", column = "id"),
        @FieldResult(name = "aberto", column = "aberto"),
        @FieldResult(name = "dataHoraAbertura", column = "data_hora_abertura"),
        @FieldResult(name = "valorInicialTroco", column = "valor_inicial_troco"),
        @FieldResult(name = "dataHoraFechamento", column = "data_hora_fechamento"),
        @FieldResult(name = "usuarioId", column = "usuario_id"),
        @FieldResult(name = "tenant", column = "tenant")
      }
    )
  }
)
public class Caixadto implements Serializable {
  
  @Id
  private Long id;
  private Boolean aberto;
  private LocalDateTime dataHoraAbertura;
  private BigDecimal valorInicialTroco;
  private LocalDateTime dataHoraFechamento;
  private String tenant;
  private Long usuarioId;
  
}


package com.store.drinks.entidade.dto.mensagem;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mensagemdto implements Serializable {

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


package com.store.drinks.entidade.dto.usuario;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioMensagemdto implements Serializable {
  
  private Long id;
  private String text;
  private String nome;
  private String destinatario;
  private String tenant;
}

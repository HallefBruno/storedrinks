
package com.store.drinks.entidade.dto;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import lombok.Data;

@Data
@Entity
@SqlResultSetMapping(
  name="Usuariodto",
  classes={
    @ConstructorResult(
      targetClass=Usuariodto.class,
      columns={
        @ColumnResult(name="id"),
        @ColumnResult(name="text"),
        @ColumnResult(name="nome"),
        @ColumnResult(name="destinatario"),
        @ColumnResult(name="tenant")
      }
    )
  }
)
public class Usuariodto implements Serializable {
  
  @Id
  private BigInteger id;
  private String text;
  private String nome;
  private String destinatario;
  private String tenant;

  public Usuariodto(BigInteger id, String text, String nome, String destinatario, String tenant) {
    this.id = id;
    this.text = text;
    this.nome = nome;
    this.destinatario = destinatario;
    this.tenant = tenant;
  }

  public Usuariodto() {
  }
  
  
}

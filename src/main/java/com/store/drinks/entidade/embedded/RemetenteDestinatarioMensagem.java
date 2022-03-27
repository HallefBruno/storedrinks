
package com.store.drinks.entidade.embedded;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class RemetenteDestinatarioMensagem implements Serializable {
  
  @NotBlank(message = "Remetente não pode ter espaços em branco!")
  @NotEmpty(message = "Remetente não pode ser vazio!")
  @NotNull(message = "Remetente não pode ser null!")
  @Column(nullable = false, length = 255)
  private String remetente;
  
  @NotBlank(message = "Destinatário não pode ter espaços em branco!")
  @NotEmpty(message = "Destinatário não pode ser vazio!")
  @NotNull(message = "Destinatário não pode ser null!")
  private String destinatario;
  
  @NotBlank(message = "Mensagem não pode ter espaços em branco!")
  @NotEmpty(message = "Mensagem não pode ser vazio!")
  @NotNull(message = "Mensagem não pode ser null!")
  @Size(min = 5, max = 255, message = "A mensagem deve estar entre 5 e 255 caracters")
  private String mensagem;
  
  public String getRemetente() {
    return remetente;
  }

  public void setRemetente(String remetente) {
    this.remetente = remetente;
  }

  public String getDestinatario() {
    return destinatario;
  }

  public void setDestinatario(String destinatario) {
    this.destinatario = destinatario;
  }

  public String getMensagem() {
    return mensagem;
  }

  public void setMensagem(String mensagem) {
    this.mensagem = mensagem;
  }
}

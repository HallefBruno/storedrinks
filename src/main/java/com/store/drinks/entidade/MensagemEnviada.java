
package com.store.drinks.entidade;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.store.drinks.entidade.embedded.RemetenteDestinatarioMensagem;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "mensagem_enviada")
public class MensagemEnviada implements Serializable {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mensagem_enviada_generator")
  @SequenceGenerator(name="mensagem_enviada_generator", sequenceName = "mensagen_enviada_seq", allocationSize = 1, initialValue = 1)
  private Long id;
  
  @Embedded
  @AttributeOverrides(value = {
    @AttributeOverride(name = "remetente", column = @Column(name = "remetente")),
    @AttributeOverride(name = "destinatario", column = @Column(name = "destinatario")),
    @AttributeOverride(name = "mensagem", column = @Column(name = "mensagem"))
  })
  private RemetenteDestinatarioMensagem remetenteDestinatarioMensagem;
  
  @Column(nullable = false, name = "data_hora_mensagem_enviada")
  private LocalDateTime dataHoraMensagemEnviada;
  
  @JoinColumn(name = "usuario_destinatario_id")
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference
  private Usuario usuario;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public RemetenteDestinatarioMensagem getRemetenteDestinatarioMensagem() {
    return remetenteDestinatarioMensagem;
  }

  public void setRemetenteDestinatarioMensagem(RemetenteDestinatarioMensagem remetenteDestinatarioMensagem) {
    this.remetenteDestinatarioMensagem = remetenteDestinatarioMensagem;
  }

  public LocalDateTime getDataHoraMensagemEnviada() {
    return dataHoraMensagemEnviada;
  }

  public void setDataHoraMensagemEnviada(LocalDateTime dataHoraMensagemEnviada) {
    this.dataHoraMensagemEnviada = dataHoraMensagemEnviada;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  
}

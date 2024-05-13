
package com.store.drinks.entidade;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.store.drinks.entidade.dto.Mensagemdto;
import com.store.drinks.entidade.embedded.RemetenteDestinatarioMensagem;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "mensagem_recebida")
@DynamicUpdate
@NamedEntityGraph(
  name = "graph.Mensagem", 
  attributeNodes = @NamedAttributeNode(value = "usuario", subgraph = "subgraph.usuario"), 
  subgraphs = {
    @NamedSubgraph(name = "subgraph.usuario", attributeNodes = @NamedAttributeNode(value = "clienteSistema", subgraph = "subgraph.clienteSistema"))
  }
)

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

public class MensagemRecebida implements Serializable {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mensagem_recebida_generator")
  @SequenceGenerator(name="mensagem_recebida_generator", sequenceName = "mensagem_recebida_seq", allocationSize = 1, initialValue = 1)
  @Column(updatable = false, unique = true, nullable = false)
  private Long id;
  
  @Embedded
  @AttributeOverrides(value = {
    @AttributeOverride(name = "remetente", column = @Column(name = "remetente")),
    @AttributeOverride(name = "destinatario", column = @Column(name = "destinatario")),
    @AttributeOverride(name = "mensagem", column = @Column(name = "mensagem", length = 400, nullable = false))
  })
  private RemetenteDestinatarioMensagem remetenteDestinatarioMensagem;

  @Column(columnDefinition = "boolean default false", nullable = false)
  private Boolean lida;
  
  @Column(columnDefinition = "boolean default false", nullable = false)
  private Boolean notificado;
  
  @Column(nullable = false, name = "data_hora_mensagem_recebida")
  private LocalDateTime dataHoraMensagemRecebida;

  @JoinColumn(name = "usuario_remetente_id")
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

  public Boolean getLida() {
    return lida;
  }

  public void setLida(Boolean lida) {
    this.lida = lida;
  }

  public Boolean getNotificado() {
    return notificado;
  }

  public void setNotificado(Boolean notificado) {
    this.notificado = notificado;
  }

  public LocalDateTime getDataHoraMensagemRecebida() {
    return dataHoraMensagemRecebida;
  }

  public void setDataHoraMensagemRecebida(LocalDateTime dataHoraMensagemRecebida) {
    this.dataHoraMensagemRecebida = dataHoraMensagemRecebida;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

}
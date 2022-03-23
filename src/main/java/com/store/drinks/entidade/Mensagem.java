package com.store.drinks.entidade;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.store.drinks.repository.util.Multitenancy;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@NamedEntityGraph(
  name = "graph.Mensagem", 
  attributeNodes = @NamedAttributeNode(value = "usuario", subgraph = "subgraph.usuario"), 
  subgraphs = {
    @NamedSubgraph(name = "subgraph.usuario", attributeNodes = @NamedAttributeNode(value = "clienteSistema", subgraph = "subgraph.clienteSistema"))
  }
)
public class Mensagem implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false, unique = true, nullable = false)
  private Long id;
  
  @NotBlank(message = "Tenant não pode ter espaços em branco!")
  @NotEmpty(message = "Tenant não pode ser vazio!")
  @NotNull(message = "Tenant não pode ser null!")
  @Column(nullable = false, length = 50)
  private String tenant;
  
  @NotBlank(message = "Destinatário não pode ter espaços em branco!")
  @NotEmpty(message = "Destinatário não pode ser vazio!")
  @NotNull(message = "Destinatário não pode ser null!")
  @Column(nullable = false, length = 50)
  private String destinatario;
  
  @NotBlank(message = "Mensagem não pode ter espaços em branco!")
  @NotEmpty(message = "Mensagem não pode ser vazio!")
  @NotNull(message = "Mensagem não pode ser null!")
  @Size(min = 10, max = 255, message = "A mensagem deve estar entre 10 e 255 caracters")
  @Column(nullable = false, length = 255)
  private String mensagem;

  @Column(columnDefinition = "boolean default false", nullable = false)
  private Boolean lida;
  
  @Column(columnDefinition = "boolean default false", nullable = false)
  private Boolean notificado;
  
  @Column(nullable = false, name = "data_hora_mensagem_recebida")
  private LocalDateTime dataHoraMensagemRecebida;
  
  @JoinColumn
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference
  private Usuario usuario;

  public Mensagem() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
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

  @PrePersist
  @PreUpdate
  private void prePersistPreUpdate() {
    this.destinatario = StringUtils.strip(this.destinatario);
    this.mensagem = StringUtils.strip(this.mensagem);
    this.mensagem = this.mensagem.toLowerCase();
    if (Objects.isNull(this.lida)) {
      this.lida = Boolean.FALSE;
    }
    this.tenant = new Multitenancy().getTenantValue();
    this.tenant = StringUtils.strip(this.tenant);
  }

}

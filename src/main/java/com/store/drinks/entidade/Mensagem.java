package com.store.drinks.entidade;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Mensagem extends ETenant implements Serializable {

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
  
  @Column(nullable = false, name = "data_hora_mensagem_recebida")
  private LocalDateTime dataHoraMensagemRecebida;

  @PrePersist
  @PreUpdate
  private void prePersistPreUpdate() {
    this.destinatario = StringUtils.strip(this.destinatario);
    this.mensagem = StringUtils.strip(this.mensagem);
    this.mensagem = this.mensagem.toLowerCase();
    if (Objects.isNull(this.lida)) {
      this.lida = Boolean.FALSE;
    }
    this.tenant = getTenantValue();
    this.tenant = StringUtils.strip(this.tenant);
  }

}

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
import javax.persistence.SequenceGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

@Data
@Entity
@EqualsAndHashCode
public class ValidarCliente implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "validar_cliente_generator")
  @SequenceGenerator(name="validar_cliente_generator", sequenceName = "validar_cliente_seq", allocationSize = 1, initialValue = 1)
  @Column(updatable = false, unique = true, nullable = false)
  private Long id;

  @Column(name = "data_validacao", nullable = false)
  private LocalDateTime dataValidacao;

  @Column(name = "cpfCnpj", nullable = false, unique = true, length = 24)
  private String cpfCnpj;

  @Column(name = "conta_criada", columnDefinition = "boolean default false")
  private Boolean contaCriada;

  @PrePersist
  @PreUpdate
  private void prePersistPreUpdate() {
    this.cpfCnpj = StringUtils.getDigits(this.cpfCnpj);
    if (Objects.isNull(this.contaCriada)) {
      this.contaCriada = Boolean.FALSE;
    }
  }

}

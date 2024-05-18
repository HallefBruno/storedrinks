package com.store.drinks.entidade;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Entity
public class MercadoLivreCode implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mercado_livre_code_generator")
  @SequenceGenerator(name = "mercado_livre_code_generator", sequenceName = "mercado_livre_code_seq", allocationSize = 1, initialValue = 1)
  private Long id;

  @Column(nullable = false, length = 200)
  private String code;
  
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "usuario_id")
  private Usuario usuario;

  @PrePersist
  @PreUpdate
  private void prePersistPreUpdate() {
    this.code = StringUtils.strip(this.code);
  }

}
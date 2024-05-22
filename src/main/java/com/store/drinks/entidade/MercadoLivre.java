package com.store.drinks.entidade;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.store.drinks.util.Multitenancy;
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
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
@EqualsAndHashCode
public class MercadoLivre implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mercado_livre_code_generator")
  @SequenceGenerator(name = "mercado_livre_code_generator", sequenceName = "mercado_livre_code_seq", allocationSize = 1, initialValue = 1)
  private Long id;

  @Column(nullable = false, length = 200)
  private String code;
  
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "usuario_id")
  @JsonManagedReference
  private Usuario usuario;
  
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(nullable = true, name = "plataformas_ecommerce_id")
  private PlataformasEcommerce plataformasEcommerce;
  
  @OneToOne(fetch = FetchType.EAGER, mappedBy = "mercadoLivre")
  private AccessTokenMeli accessTokenMeli;
  
  @Column(nullable = false, unique = true, length = 50)
  private String tenant;

  @PrePersist
  @PreUpdate
  private void prePersistPreUpdate() {
    this.code = StringUtils.strip(this.code);
    this.tenant = new Multitenancy().getTenantValue().trim();
  }

}
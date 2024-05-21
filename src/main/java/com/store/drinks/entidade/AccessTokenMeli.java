package com.store.drinks.entidade;

import com.store.drinks.entidade.MercadoLivre;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@EqualsAndHashCode
@DynamicUpdate
public class AccessTokenMeli implements Serializable {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "access_token_meli_generator")
  @SequenceGenerator(name="access_token_meli_generator", sequenceName = "access_token_meli_seq", allocationSize = 1, initialValue = 1)
  @Column(updatable = false, unique = true, nullable = false)
  private Long id;
  
  @Column(nullable = false, name = "access_token")
  private String accessToken;
  
  @Column(nullable = false, name = "token_type")
  private String tokenType;
  
  @Column(nullable = false, name = "expires_in")
  private Integer expiresIn;
  
  @Column(nullable = false, name = "scope")
  private String scope;
  
  @Column(nullable = false, name = "user_id")
  private Long userId;
  
  @Column(nullable = false, name = "refresh_token")
  private String refreshToken;
  
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false, name = "mercado_livre_id")
  private MercadoLivre mercadoLivre;
  
  
}

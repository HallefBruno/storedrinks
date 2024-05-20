package com.store.drinks.entidade;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
@EqualsAndHashCode
public class PlataformasEcommerce implements Serializable {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plataformas_integracao_generator")
  @SequenceGenerator(name="plataformas_integracao_generator", sequenceName = "plataformas_integracao_seq", allocationSize = 1, initialValue = 1)
  private Long id;
  
  @Column(name = "descricao", nullable = false)
  private String descricao;
  
  @Column(name = "data_cadastro", nullable = false)
  private LocalDate dataCadastro;
  
  @Column(name = "ativo", nullable = false)
  private Boolean ativo;
  
  @Column(name = "imagem", nullable = false, length = 100)
  private String imagem;
  
  @Column(name = "html_id_button", nullable = false, length = 100)
  private String htmlIdButton;
  
  @Column(name = "html_class", nullable = false, length = 100)
  private String htmlClass;
  
  @Column(name = "html_style", nullable = false, length = 300)
  private String htmlStyle;
  
  @Column(name = "implantada", nullable = false)
  private Boolean implantada;

  public PlataformasEcommerce() {
  }
  
  private PlataformasEcommerce(Boolean ativo) {
    this.ativo = ativo;
  }
  
  public static PlataformasEcommerce of(Boolean ativo) {
    return new PlataformasEcommerce(ativo);
  }

  
}

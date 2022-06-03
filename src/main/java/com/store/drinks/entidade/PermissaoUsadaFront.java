

package com.store.drinks.entidade;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@Table(name = "permissao_usada_front")
@DynamicUpdate
public class PermissaoUsadaFront implements Serializable {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotBlank(message = "Permissão obrigatória!")
  private String permissao;
  @NotBlank(message = "Diretório obrigatório!")
  private String diretorio;
  @NotBlank(message = "Url obrigatória!")
  private String url;
  @NotBlank(message = "Descrição obrigatória!")
  private String descricao;
  
  @PrePersist
  @PreUpdate
  private void prePersistPreUpdate() {
    this.diretorio = StringUtils.strip(this.diretorio);
    this.url = StringUtils.strip(this.url);
    this.descricao = StringUtils.strip(this.descricao);
    //Validate.matchesPattern(this.url,"<\\b(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]>");
    
  }
  
}

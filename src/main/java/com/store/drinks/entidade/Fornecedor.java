package com.store.drinks.entidade;

import com.store.drinks.repository.util.Multitenancy;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
@NamedQuery(query = "from Fornecedor f where f.cpfCnpj = ?1 and f.tenant = ?2 ", name = "find fornecedor")
public class Fornecedor implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fornecedor_generator")
  @SequenceGenerator(name="fornecedor_generator", sequenceName = "fornecedor_seq", allocationSize = 1, initialValue = 1)
  @Column(updatable = false, unique = true, nullable = false)
  private Long id;

  @NotBlank(message = "CPF/CNPJ pode ter espaços em branco!")
  @NotEmpty(message = "CPF/CNPJ não pode ser vazio!")
  @NotNull(message = "CPF/CNPJ não pode ser null!")
  @Column(name = "cpf_cnpj", length = 20, nullable = true)
  private String cpfCnpj;

  @NotBlank(message = "Nome não pode ter espaços em branco!")
  @NotEmpty(message = "Nome não pode ser vazio!")
  @NotNull(message = "Nome não pode ser null!")
  @Column(length = 200, nullable = true)
  private String nome;

  @NotBlank(message = "Telefone não pode ter espaços em branco!")
  @NotEmpty(message = "Telefone não pode ser vazio!")
  @NotNull(message = "Telefone não pode ser null!")
  @Column(length = 12, nullable = true)
  private String telefone;

  @Column(nullable = false, updatable = false, length = 50)
  private String tenant;

  @Column(columnDefinition = "boolean default false")
  private Boolean ativo;

  @Version
  @Column(name = "versao_objeto", nullable = false)
  private Integer versaoObjeto;

  @PrePersist
  @PreUpdate
  private void prePersistPreUpdate() {
    this.cpfCnpj = StringUtils.getDigits(this.cpfCnpj);
    this.telefone = StringUtils.getDigits(this.telefone);//phoneNumberstr.replaceAll("[^0-9]", "");
    this.nome = StringUtils.strip(this.nome);
    if (Objects.isNull(this.ativo)) {
      this.ativo = Boolean.FALSE;
    }
    this.tenant = new Multitenancy().getTenantValue();
    this.tenant = StringUtils.strip(this.tenant);
  }
}

package com.store.drinks.entidade;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

@Data
@Entity
@NamedEntityGraph(name = "graph.Usuario.clienteSistema", attributeNodes = @NamedAttributeNode("clienteSistema"))
@EqualsAndHashCode
public class Usuario implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false, unique = true, nullable = false)
  private Long id;
  
  @NotEmpty(message = "Nome não pode ser vazio!")
  @NotNull(message = "Nome não pode ser null!")
  @NotBlank(message = "Nome é obrigatório")
  private String nome;

  @NotBlank(message = "E-mail é obrigatório")
  @Email(message = "E-mail inválido")
  @Column(unique = true)
  private String email;
  
  @NotEmpty(message = "Senha não pode ser vazio!")
  @NotNull(message = "Senha não pode ser null!")
  @NotBlank(message = "Senha é obrigatório")
  private String senha;

  @Transient
  private String confirmacaoSenha;
  
  private Boolean ativo;

  @Size(min = 1, message = "Selecione pelo menos um grupo")
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "usuario_grupo", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name = "id_grupo"))
  private Set<Grupo> grupos;

  @Column(name = "data_nascimento")
  private LocalDate dataNascimento;

  @Column(nullable = false)
  private Boolean proprietario;

  @JoinColumn(name = "tenant", referencedColumnName = "tenant", nullable = false)
  @ManyToOne
  private ClienteSistema clienteSistema;

  @PreUpdate
  @PrePersist
  private void prePersistPreUpdate() {
    this.confirmacaoSenha = senha;
    this.email = StringUtils.strip(this.email);
    if (Objects.isNull(this.ativo)) {
      this.ativo = Boolean.FALSE;
    }
    this.nome = this.nome.toLowerCase();
    this.email = this.email.toLowerCase();
  }

}

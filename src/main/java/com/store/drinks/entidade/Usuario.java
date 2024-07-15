package com.store.drinks.entidade;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
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
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;
import com.store.drinks.entidade.dto.usuario.UsuarioMensagemdto;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@DynamicUpdate
@EqualsAndHashCode
@NamedEntityGraph(name = "graph.Usuario.clienteSistema", attributeNodes = @NamedAttributeNode("clienteSistema"))
@SqlResultSetMapping(
  name="UsuarioMensagemdto",
  classes={ 
    @ConstructorResult(targetClass=UsuarioMensagemdto.class,
    columns={
      @ColumnResult(name = "id", type=Long.class),
      @ColumnResult(name = "text", type=String.class),
      @ColumnResult(name = "nome", type=String.class),
      @ColumnResult(name = "destinatario", type=String.class),
      @ColumnResult(name = "tenant", type=String.class)
    })
  }
)
public class Usuario implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_generator")
  @SequenceGenerator(name="usuario_generator", sequenceName = "usuario_seq", allocationSize = 1, initialValue = 1)
  @Column(updatable = false, unique = true, nullable = false)
  private Long id;
  
  @NotEmpty(message = "Nome não pode ser vazio")
  @NotNull(message = "Nome não pode ser null")
  @NotBlank(message = "Nome é obrigatório")
  private String nome;

  @NotBlank(message = "E-mail é obrigatório")
  @Email(message = "E-mail inválido")
  @Column(unique = true)
  private String email;
  
  @Column(length = 80, nullable = false, unique = true)
  private String senha;
  
  @NotEmpty(message = "Telefone não pode ser vazio")
  @NotNull(message = "Telefone não pode ser null")
  @NotBlank(message = "Telefone é obrigatório")
  @Column(unique = true, length = 15)
  private String telefone;

  @Transient
  private String confirmacaoSenha;
  
  @Column
  private Boolean ativo;
  
  @Column(length = 100)
  private String imagem;
  
  @Column(length = 5)
  private String extensao;

  @Size(min = 1, message = "Selecione pelo menos um grupo")
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "usuario_grupo", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name = "id_grupo"))
  private Set<Grupo> grupos;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
  @JsonManagedReference
  private Set<MensagemEnviada> mensagensEnviadas;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
  @JsonManagedReference
  private Set<MensagemRecebida> mensagensRecebidas;
  
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "data_nascimento")
  private LocalDate dataNascimento;

  @Column(nullable = false, columnDefinition = "boolean default false")
  private Boolean proprietario;
  
  @JoinColumn(name = "tenant", referencedColumnName = "tenant", nullable = false)
  @ManyToOne(fetch = FetchType.EAGER)
  @JsonBackReference
  private ClienteSistema clienteSistema;
  
  @JoinColumn(name = "mercado_livre_id", nullable = true)
  @ManyToOne(fetch = FetchType.EAGER)
  @JsonBackReference
  private MercadoLivre mercadoLivre;
  
  @Column(nullable = false, columnDefinition = "boolean default false")
  private Boolean adm;
  
  public Boolean meliImplantadoIsUserAdmin() {
    return mercadoLivre != null && adm;
  }
  
  @PreUpdate
  @PrePersist
  private void prePersistPreUpdate() {
    this.confirmacaoSenha = senha;
    this.email = StringUtils.strip(this.email);
    this.imagem = StringUtils.strip(this.imagem);
    this.nome = StringUtils.strip(this.nome);
    this.telefone = StringUtils.getDigits(this.telefone);
    if (Objects.isNull(this.ativo)) {
      this.ativo = Boolean.FALSE;
    }
    if(Objects.isNull(this.proprietario)) {
      this.proprietario = Boolean.FALSE;
    }
    if (Objects.isNull(this.adm)) {
      this.adm = Boolean.FALSE;
    }
  }
  
}

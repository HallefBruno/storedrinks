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
import javax.persistence.OneToMany;
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
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
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
  
  @NotEmpty(message = "Senha não pode ser vazio")
  @NotNull(message = "Senha não pode ser null")
  @NotBlank(message = "Senha é obrigatório")
  @Min(value = 11, message = "Senha precisa ter no mínino 11 caracteres")
  @Max(value = 11, message = "Senha precisa ter no máximo 11 caracteres")
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
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "usuario_grupo", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name = "id_grupo"))
  private Set<Grupo> grupos;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario", orphanRemoval = true)
  @JsonManagedReference
  private Set<MensagemEnviada> mensagensEnviadas;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario", orphanRemoval = true)
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getConfirmacaoSenha() {
    return confirmacaoSenha;
  }

  public void setConfirmacaoSenha(String confirmacaoSenha) {
    this.confirmacaoSenha = confirmacaoSenha;
  }

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean ativo) {
    this.ativo = ativo;
  }

  public String getImagem() {
    return imagem;
  }

  public void setImagem(String imagem) {
    this.imagem = imagem;
  }

  public Set<Grupo> getGrupos() {
    return grupos;
  }

  public void setGrupos(Set<Grupo> grupos) {
    this.grupos = grupos;
  }

  public Set<MensagemEnviada> getMensagensEnviadas() {
    return mensagensEnviadas;
  }

  public void setMensagensEnviadas(Set<MensagemEnviada> mensagensEnviadas) {
    this.mensagensEnviadas = mensagensEnviadas;
  }

  public Set<MensagemRecebida> getMensagensRecebidas() {
    return mensagensRecebidas;
  }

  public void setMensagensRecebidas(Set<MensagemRecebida> mensagensRecebidas) {
    this.mensagensRecebidas = mensagensRecebidas;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public Boolean getProprietario() {
    return proprietario;
  }

  public void setProprietario(Boolean proprietario) {
    this.proprietario = proprietario;
  }

  public ClienteSistema getClienteSistema() {
    return clienteSistema;
  }

  public void setClienteSistema(ClienteSistema clienteSistema) {
    this.clienteSistema = clienteSistema;
  }

  public String getExtensao() {
    return extensao;
  }

  public void setExtensao(String extensao) {
    this.extensao = extensao;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 53 * hash + Objects.hashCode(this.id);
    hash = 53 * hash + Objects.hashCode(this.nome);
    hash = 53 * hash + Objects.hashCode(this.email);
    hash = 53 * hash + Objects.hashCode(this.senha);
    hash = 53 * hash + Objects.hashCode(this.telefone);
    hash = 53 * hash + Objects.hashCode(this.confirmacaoSenha);
    hash = 53 * hash + Objects.hashCode(this.ativo);
    hash = 53 * hash + Objects.hashCode(this.imagem);
    hash = 53 * hash + Objects.hashCode(this.extensao);
    hash = 53 * hash + Objects.hashCode(this.grupos);
    hash = 53 * hash + Objects.hashCode(this.mensagensEnviadas);
    hash = 53 * hash + Objects.hashCode(this.mensagensRecebidas);
    hash = 53 * hash + Objects.hashCode(this.dataNascimento);
    hash = 53 * hash + Objects.hashCode(this.proprietario);
    hash = 53 * hash + Objects.hashCode(this.clienteSistema);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Usuario other = (Usuario) obj;
    if (!Objects.equals(this.nome, other.nome)) {
      return false;
    }
    if (!Objects.equals(this.email, other.email)) {
      return false;
    }
    if (!Objects.equals(this.senha, other.senha)) {
      return false;
    }
    if (!Objects.equals(this.telefone, other.telefone)) {
      return false;
    }
    if (!Objects.equals(this.confirmacaoSenha, other.confirmacaoSenha)) {
      return false;
    }
    if (!Objects.equals(this.imagem, other.imagem)) {
      return false;
    }
    if (!Objects.equals(this.extensao, other.extensao)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.ativo, other.ativo)) {
      return false;
    }
    if (!Objects.equals(this.grupos, other.grupos)) {
      return false;
    }
    if (!Objects.equals(this.mensagensEnviadas, other.mensagensEnviadas)) {
      return false;
    }
    if (!Objects.equals(this.mensagensRecebidas, other.mensagensRecebidas)) {
      return false;
    }
    if (!Objects.equals(this.dataNascimento, other.dataNascimento)) {
      return false;
    }
    if (!Objects.equals(this.proprietario, other.proprietario)) {
      return false;
    }
    return Objects.equals(this.clienteSistema, other.clienteSistema);
  }

  @Override
  public String toString() {
    return "Usuario{" + "id=" + id + ", nome=" + nome + ", email=" + email + ", senha=" + senha + ", telefone=" + telefone + ", confirmacaoSenha=" + confirmacaoSenha + ", ativo=" + ativo + ", imagem=" + imagem + ", extensao=" + extensao + ", grupos=" + grupos + ", mensagensEnviadas=" + mensagensEnviadas + ", mensagensRecebidas=" + mensagensRecebidas + ", dataNascimento=" + dataNascimento + ", proprietario=" + proprietario + ", clienteSistema=" + clienteSistema + '}';
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
  }
  
}

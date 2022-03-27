package com.store.drinks.entidade;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;

@Entity
@NamedEntityGraph(name = "graph.Usuario.clienteSistema", attributeNodes = @NamedAttributeNode("clienteSistema"))
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
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "usuario_grupo", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name = "id_grupo"))
  private Set<Grupo> grupos;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario", orphanRemoval = true)
  @JsonManagedReference
  private Set<MensagensEnviadas> mensagensEnviadas;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario", orphanRemoval = true)
  @JsonManagedReference
  private Set<MensagensRecebidas> mensagensRecebidas;
  
  @Column(name = "data_nascimento")
  private LocalDate dataNascimento;

  @Column(nullable = false)
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

  public Set<Grupo> getGrupos() {
    return grupos;
  }

  public void setGrupos(Set<Grupo> grupos) {
    this.grupos = grupos;
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

  public Set<MensagensEnviadas> getMensagensEnviadas() {
    return mensagensEnviadas;
  }

  public void setMensagensEnviadas(Set<MensagensEnviadas> mensagensEnviadas) {
    this.mensagensEnviadas = mensagensEnviadas;
  }

  public Set<MensagensRecebidas> getMensagensRecebidas() {
    return mensagensRecebidas;
  }

  public void setMensagensRecebidas(Set<MensagensRecebidas> mensagensRecebidas) {
    this.mensagensRecebidas = mensagensRecebidas;
  }

  public ClienteSistema getClienteSistema() {
    return clienteSistema;
  }

  public void setClienteSistema(ClienteSistema clienteSistema) {
    this.clienteSistema = clienteSistema;
  }

  
  
  
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

package com.store.drinks.entidade;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Table(name = "cliente_sistema")
public class ClienteSistema implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_sistema_generator")
  @SequenceGenerator(name="cliente_sistema_generator", sequenceName = "cliente_sistema_seq", allocationSize = 1, initialValue = 1)
  @Column(updatable = false, unique = true, nullable = false)
  private Long id;

  @NotBlank(message = "Nome do comercio não pode ter espaços em branco!")
  @NotEmpty(message = "Nome do comercio não pode ser vazio!")
  @NotNull(message = "Nome do comercio não pode ser null!")
  @Column(name = "nome_comercio", nullable = false, length = 255)
  private String nomeComercio;

  @NotBlank(message = "CPF/CNPJ não pode ter espaços em branco!")
  @NotEmpty(message = "CPF/CNPJ não pode ser vazio!")
  @NotNull(message = "CPF/CNPJ não pode ser null!")
  @Column(name = "cpf_cnpj", nullable = false, unique = true, length = 18)
  private String cpfCnpj;

  @NotBlank(message = "Tenant não pode ter espaços em branco!")
  @NotEmpty(message = "Tenant não pode ser vazio!")
  @NotNull(message = "Tenant não pode ser null!")
  @Column(name = "tenant", nullable = false, unique = true, length = 50)
  private String tenant;

  @Column(name = "data_cadastro", nullable = false)
  private LocalDateTime dataCadastro;

  @Column(name = "data_atualizacao", nullable = false)
  private LocalDateTime dataAtualizacao;

  @NotBlank(message = "Estado não pode ter espaços em branco!")
  @NotEmpty(message = "Estado não pode ser vazio!")
  @NotNull(message = "Estado não pode ser null!")
  @Column(name = "estado", nullable = false, length = 100)
  private String estado;

  @NotBlank(message = "Cidade não pode ter espaços em branco!")
  @NotEmpty(message = "Cidade não pode ser vazio!")
  @NotNull(message = "Cidade não pode ser null!")
  @Column(name = "cidade", nullable = false, length = 200)
  private String cidade;

  @NotBlank(message = "Bairro não pode ter espaços em branco!")
  @NotEmpty(message = "Bairro não pode ser vazio!")
  @NotNull(message = "Bairro não pode ser null!")
  @Column(name = "bairro", nullable = false, length = 200)
  private String bairro;

  @NotBlank(message = "CEP não pode ter espaços em branco!")
  @NotEmpty(message = "CEP não pode ser vazio!")
  @NotNull(message = "CEP não pode ser null!")
  @Column(nullable = false, length = 10)
  private String cep;

  @NotBlank(message = "Logradouro não pode ter espaços em branco!")
  @NotEmpty(message = "Logradouro não pode ser vazio!")
  @NotNull(message = "Logradouro não pode ser null!")
  @Column(nullable = false, length = 100)
  private String logradouro;

  @NotNull(message = "Quantidade usuário não pode ser null!")
  @Column(name = "qtd_usuario", nullable = false)
  private Integer qtdUsuario;

  @Column(name = "acessar_tela_criar_login", columnDefinition = "boolean default false")
  private Boolean acessarTelaCriarLogin;

  @Column(name = "primeiro_acesso", columnDefinition = "boolean default false")
  private Boolean primeiroAcesso;
  
  @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "clienteSistema", orphanRemoval = true)
  @JsonManagedReference
  private Set<Usuario> usuarios;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNomeComercio() {
    return nomeComercio;
  }

  public void setNomeComercio(String nomeComercio) {
    this.nomeComercio = nomeComercio;
  }

  public String getCpfCnpj() {
    return cpfCnpj;
  }

  public void setCpfCnpj(String cpfCnpj) {
    this.cpfCnpj = cpfCnpj;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  public LocalDateTime getDataCadastro() {
    return dataCadastro;
  }

  public void setDataCadastro(LocalDateTime dataCadastro) {
    this.dataCadastro = dataCadastro;
  }

  public LocalDateTime getDataAtualizacao() {
    return dataAtualizacao;
  }

  public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
    this.dataAtualizacao = dataAtualizacao;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public String getLogradouro() {
    return logradouro;
  }

  public void setLogradouro(String logradouro) {
    this.logradouro = logradouro;
  }

  public Integer getQtdUsuario() {
    return qtdUsuario;
  }

  public void setQtdUsuario(Integer qtdUsuario) {
    this.qtdUsuario = qtdUsuario;
  }

  public Boolean getAcessarTelaCriarLogin() {
    return acessarTelaCriarLogin;
  }

  public void setAcessarTelaCriarLogin(Boolean acessarTelaCriarLogin) {
    this.acessarTelaCriarLogin = acessarTelaCriarLogin;
  }

  public Boolean getPrimeiroAcesso() {
    return primeiroAcesso;
  }

  public void setPrimeiroAcesso(Boolean primeiroAcesso) {
    this.primeiroAcesso = primeiroAcesso;
  }

  public Set<Usuario> getUsuarios() {
    return usuarios;
  }

  public void setUsuarios(Set<Usuario> usuarios) {
    this.usuarios.clear();
    this.usuarios.addAll(usuarios);
  }
  
  @PreUpdate
  @PrePersist
  private void prePersistPreUpdate() {
    this.bairro = StringUtils.strip(this.bairro);
    this.bairro = this.bairro.toLowerCase();
    this.cidade = StringUtils.strip(this.cidade);
    this.cidade = this.cidade.toLowerCase();
    this.estado = StringUtils.strip(this.estado);
    this.estado = this.estado.toLowerCase();
    this.nomeComercio = StringUtils.strip(this.nomeComercio);
    this.nomeComercio = this.nomeComercio.toLowerCase();
    this.logradouro = StringUtils.strip(this.logradouro);
    this.logradouro = this.logradouro.toLowerCase();
    this.cep = StringUtils.getDigits(this.cep);
    this.cpfCnpj = StringUtils.getDigits(this.cpfCnpj);

    if (Objects.isNull(this.acessarTelaCriarLogin)) {
      this.acessarTelaCriarLogin = Boolean.FALSE;
    }
    if (Objects.isNull(this.primeiroAcesso)) {
      this.primeiroAcesso = Boolean.FALSE;
    }
  }
}

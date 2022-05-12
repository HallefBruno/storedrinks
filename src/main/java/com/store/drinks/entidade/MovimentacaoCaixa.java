package com.store.drinks.entidade;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.store.drinks.repository.util.Multitenancy;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Entity
@Table(name = "movimentacao_caixa")
@DynamicUpdate
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovimentacaoCaixa implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false, unique = true, nullable = false)
  private Long id;
  
  @NotNull(message = "Valor recebido é obrigatório!")
  @Min(value = 0, message = "Valor recebido não pode ser negativo!")
  @Column(name = "valor_recebido", nullable = false)
  private BigDecimal valorRecebido;
  
  @NotNull(message = "Valor de troco é obrigatório!")
  @Min(value = 0, message = "Valor mínimo não pode ser negativo!")
  @Column(name = "valor_troco", nullable = false)
  private BigDecimal valorTroco;
  
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "movimentacaoCaixa")
  @JsonBackReference
  private Set<FormaPagamento> formaPagamentos;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Caixa caixa;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Usuario usuario;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(nullable = true)
  private Venda venda;
  
  @Column(nullable = false, columnDefinition = "boolean default false")
  private Boolean recolhimento;

  @Column(nullable = false, updatable = false, length = 50)
  private String tenant;

  @PrePersist
  @PreUpdate
  private void prePersistPreUpdate() {
    this.tenant = new Multitenancy().getTenantValue();
    this.tenant = StringUtils.strip(this.tenant);
    
    if (Objects.isNull(this.valorRecebido)) {
      this.valorRecebido = BigDecimal.ZERO;
    }
    if (Objects.isNull(this.valorTroco)) {
      this.valorTroco = BigDecimal.ZERO;
    }
    if (Objects.isNull(this.recolhimento)) {
      this.recolhimento = Boolean.FALSE;
    }
    if (Objects.isNull(this.venda) && this.recolhimento.equals(Boolean.FALSE)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movimentação de caixa inválida!");
    }
    
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + Objects.hashCode(this.id);
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
    final MovimentacaoCaixa other = (MovimentacaoCaixa) obj;
    return Objects.equals(this.id, other.id);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getValorRecebido() {
    return valorRecebido;
  }

  public void setValorRecebido(BigDecimal valorRecebido) {
    this.valorRecebido = valorRecebido;
  }

  public BigDecimal getValorTroco() {
    return valorTroco;
  }

  public void setValorTroco(BigDecimal valorTroco) {
    this.valorTroco = valorTroco;
  }

  public Set<FormaPagamento> getFormaPagamentos() {
    return formaPagamentos;
  }

  public void setFormaPagamentos(Set<FormaPagamento> formaPagamentos) {
    this.formaPagamentos = formaPagamentos;
  }

  public Caixa getCaixa() {
    return caixa;
  }

  public void setCaixa(Caixa caixa) {
    this.caixa = caixa;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public Venda getVenda() {
    return venda;
  }

  public void setVenda(Venda venda) {
    this.venda = venda;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  public Boolean getRecolhimento() {
    return recolhimento;
  }

  public void setRecolhimento(Boolean recolhimento) {
    this.recolhimento = recolhimento;
  }
  
}

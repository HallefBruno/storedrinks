package com.store.drinks.entidade;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.store.drinks.repository.util.Multitenancy;
import com.store.drinks.entidade.dto.venda.ItensVendaCancelardto;
import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "itens_venda")
@DynamicUpdate
@SqlResultSetMapping(
  name = "ItensVendaCancelardto",
  classes = {
    @ConstructorResult(targetClass = ItensVendaCancelardto.class,
      columns = {
        @ColumnResult(name = "id", type=Long.class),
        @ColumnResult(name = "quantidade", type=Integer.class),
        @ColumnResult(name = "descricao_produto", type=String.class),
        @ColumnResult(name = "valor_venda", type=BigDecimal.class),
        @ColumnResult(name = "valor_total_venda", type=BigDecimal.class)
      }
    ),
  }
)
public class ItensVenda implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itens_venda_generator")
  @SequenceGenerator(name="itens_venda_generator", sequenceName = "itens_venda_seq", allocationSize = 1, initialValue = 1)
  @Column(updatable = false, unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Produto produto;

  @NotNull(message = "Quantidade do produto é obrigatória!")
  @Min(value = 1, message = "Quantidade inválida!")
  @Column(nullable = false)
  private Integer quantidade;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  @JsonBackReference
  private Venda venda;

  @Column(nullable = false, updatable = false, length = 50)
  private String tenant;

  @PrePersist
  @PreUpdate
  private void prePersistPreUpdate() {
    this.tenant = new Multitenancy().getTenantValue();
    this.tenant = StringUtils.strip(this.tenant);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Produto getProduto() {
    return produto;
  }

  public void setProduto(Produto produto) {
    this.produto = produto;
  }

  public Integer getQuantidade() {
    return quantidade;
  }

  public void setQuantidade(Integer quantidade) {
    this.quantidade = quantidade;
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

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 47 * hash + Objects.hashCode(this.id);
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
    final ItensVenda other = (ItensVenda) obj;
    return Objects.equals(this.id, other.id);
  }
  
}

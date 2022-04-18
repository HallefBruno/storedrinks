package com.store.drinks.entidade;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.store.drinks.repository.util.Multitenancy;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Min;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
public class Venda implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false, unique = true, nullable = false)
  private Long id;

  @OneToMany(mappedBy = "venda", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonManagedReference
  private List<ItensVenda> itensVendas;

  @Column(name = "data_hora_venda", nullable = false)
  private LocalDateTime dataHoraVenda;

  @Min(value = 0, message = "Valor mínimo")
  @Column(name = "valor_total_venda", nullable = false)
  private BigDecimal valorTotalVenda;
  
  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Usuario usurio;

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

  public List<ItensVenda> getItensVendas() {
    return itensVendas;
  }

  public void setItensVendas(List<ItensVenda> itensVendas) {
    this.itensVendas = itensVendas;
  }

  public LocalDateTime getDataHoraVenda() {
    return dataHoraVenda;
  }

  public void setDataHoraVenda(LocalDateTime dataHoraVenda) {
    this.dataHoraVenda = dataHoraVenda;
  }

  public BigDecimal getValorTotalVenda() {
    return valorTotalVenda;
  }

  public void setValorTotalVenda(BigDecimal valorTotalVenda) {
    this.valorTotalVenda = valorTotalVenda;
  }

  public Usuario getUsurio() {
    return usurio;
  }

  public void setUsurio(Usuario usurio) {
    this.usurio = usurio;
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
    hash = 43 * hash + Objects.hashCode(this.id);
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
    final Venda other = (Venda) obj;
    return Objects.equals(this.id, other.id);
  }
  
}
//@JoinColumn(name = "tenant", referencedColumnName = "tenant", nullable = false, unique = true)
//@ManyToOne
//private ClienteSistema clienteSistema;
//@JoinColumn(table = "cliente_sistema", referencedColumnName = "tenant")


package com.store.drinks.entidade.dto.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalVendadto implements Serializable {
  
  private BigDecimal totalDia;
  private BigDecimal totalMes;
  private BigDecimal totalAno;

  public TotalVendadto(BigDecimal totalDia, BigDecimal totalMes, BigDecimal totalAno) {
    this.totalDia = totalDia;
    this.totalMes = totalMes;
    this.totalAno = totalAno;
  }

  public TotalVendadto() {
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 53 * hash + Objects.hashCode(this.totalDia);
    hash = 53 * hash + Objects.hashCode(this.totalMes);
    hash = 53 * hash + Objects.hashCode(this.totalAno);
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
    final TotalVendadto other = (TotalVendadto) obj;
    if (!Objects.equals(this.totalDia, other.totalDia)) {
      return false;
    }
    if (!Objects.equals(this.totalMes, other.totalMes)) {
      return false;
    }
    return Objects.equals(this.totalAno, other.totalAno);
  }

  @Override
  public String toString() {
    return "TotalVendadto{" + "totalDia=" + totalDia + ", totalMes=" + totalMes + ", totalAno=" + totalAno + '}';
  }
  
}

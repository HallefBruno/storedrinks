package com.store.drinks.entidade;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.store.drinks.entidade.enuns.TipoPagamento;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "forma_pagamento")
public class FormaPagamento implements Serializable {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @NotNull(message = "Tipo de pagamento é obrigatório!")
  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_pagamento", nullable = false)
  private TipoPagamento tipoPagamento;
  
  @NotNull(message = "Valor é obrigatório!")
  @Min(value = 0, message = "Valor inválido!")
  @Column(name = "valor", nullable = false)
  private BigDecimal valor;
  
  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.EAGER)
  @JsonBackReference
  private MovimentacaoCaixa movimentacaoCaixa;
  
  @Column(nullable = false, updatable = false, length = 50)
  private String tenant;

}

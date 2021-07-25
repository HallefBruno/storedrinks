
package com.store.drinks.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@Table(name = "movimentacao_caixa")
@DynamicUpdate
public class MovimentacaoCaixa implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;
    
    @Min(value = 0, message = "Valor mínimo")
    @Column(name = "valor_entrada", nullable = false)
    private BigDecimal valorEntrada;
    
    @Min(value = 0, message = "Valor mínimo")
    @Column(name = "valor_saida", nullable = false)
    private BigDecimal valorSaida;
    
    @NotNull(message = "Forma de pagamento não pode ser null!")
    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false)
    private FormaPagamento formaPagamento;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "abrir_caixa",nullable = false)
    private AbrirCaixa abrirCaixa;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(nullable = false)
    private Venda venda;
    
}

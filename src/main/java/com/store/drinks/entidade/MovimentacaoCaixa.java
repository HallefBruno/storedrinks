
package com.store.drinks.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
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
    
    @Column(name = "forma_pagamento", nullable = false)
    private String formaPagamento;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "open_caixa",nullable = false)
    private OpenCaixa openCaixa;
    
    @PrePersist
    @PreUpdate
    private void prePersistPreUpdate() {
        this.formaPagamento = StringUtils.strip(this.formaPagamento);
    }
}

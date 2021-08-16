
package com.store.drinks.entidade;

import com.store.drinks.entidade.enuns.FormaPagamento;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@Table(name = "movimentacao_caixa")
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class MovimentacaoCaixa extends ETenant implements Serializable {
    
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
    
    //@JoinColumn(name = "tenant", referencedColumnName = "tenant", nullable = false, unique = true)
    //@ManyToOne
    //private ClienteSistema clienteSistema;
    @JoinColumn(table = "cliente_sistema", referencedColumnName = "tenant")
    @Column(nullable = false, updatable = false, length = 20)
    private String tenant;
    
    @PrePersist
    @PreUpdate
    private void prePersistPreUpdate() {
        if(StringUtils.isBlank(this.tenant)) {
            this.tenant = getTenantValue();
        }
        this.tenant = StringUtils.strip(this.tenant);
    }
}

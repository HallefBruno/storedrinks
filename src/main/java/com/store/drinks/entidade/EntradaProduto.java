package com.store.drinks.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.NumberFormat;

@Data
@Entity
@DynamicUpdate
@Table(name = "entrada_produto")
public class EntradaProduto implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;
    
    @JoinColumn(nullable = false)
    @ManyToOne
    private Produto produto;
    
    @NotBlank(message = "Fornecedor do produto não pode ter espaços em branco!")
    @NotEmpty(message = "Fornecedor do produto não pode ser vazio!")
    @NotNull(message = "Fornecedor do produto não pode ser null!")
    @Column(length = 255,name = "fornecedor", nullable = false)
    private String fornecedor;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_emissao", nullable = false)
    private Date dataEmissao;
    
    @NotNull(message = "Quantidade não pode ser null!")
    @Min(value = 0, message = "Quantidade mínima")
    @Column(nullable = false)
    private Integer quantidade;
    
    @NotNull(message = "Valor de custo não pode ser null!")
    @Min(value = 0, message = "Valor de custo mínima")
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "###,##0.00")
    @Column(nullable = false, name = "valor_custo")
    private BigDecimal valorCusto;
    
    @NotNull(message = "Valor total não pode ser null!")
    @Min(value = 0, message = "Valor total")
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "###,##0.00")
    @Column(nullable = false, name = "valor_total")
    private BigDecimal valorTotal;
    
    @NotBlank(message = "Forma de pagamento não pode ter espaços em branco!")
    @NotEmpty(message = "Forma de pagamento não pode ser vazio!")
    @NotNull(message = "Forma de pagamento não pode ser null!")
    @Column(length = 255, name = "forma_pagamento", nullable = false)
    private String formaPagamento;
    
    @NotBlank(message = "Situacao da compra não pode ter espaços em branco!")
    @NotEmpty(message = "Situacao da compra não pode ser vazio!")
    @NotNull(message = "Situacao da compra não pode ser null!")
    @Column(name = "situacao", nullable = false, length = 50)
    private SituacaoCompra situacaoCompra;
    
    @Version
    @Column(name = "versao_objeto", nullable = false)
    private Integer versaoObjeto;
    
}

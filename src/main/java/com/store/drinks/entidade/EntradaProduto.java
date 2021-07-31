package com.store.drinks.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@DynamicUpdate
@Table(name = "entrada_produto")
@EqualsAndHashCode
public class EntradaProduto implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;
    
    @JoinColumn(nullable = false)
    @ManyToOne
    private Produto produto;
    
    @NotBlank(message = "Nº da nota não pode ter espaços em branco!")
    @NotEmpty(message = "Nº da nota não pode ser vazio!")
    @NotNull(message = "Nº da nota não pode ser null!")
    @Column(length = 255,name = "numero_nota", nullable = false, unique = true)
    private String numeroNota;
    
    @NotBlank(message = "Cnpj ou cpf não pode ter espaços em branco!")
    @NotEmpty(message = "Cnpj ou cpf não pode ser vazio!")
    @NotNull(message = "Cnpj ou cpf não pode ser null!")
    @Column(length = 20,name = "cnpjcpf", nullable = false)
    private String cnpjCpf;

    @NotBlank(message = "Fornecedor do produto não pode ter espaços em branco!")
    @NotEmpty(message = "Fornecedor do produto não pode ser vazio!")
    @NotNull(message = "Fornecedor do produto não pode ser null!")
    @Column(length = 255,name = "fornecedor", nullable = false)
    private String fornecedor;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "data_emissao", nullable = false)
    private Date dataEmissao;
    
    @NotNull(message = "Quantidade não pode ser null!")
    @Min(value = 0, message = "Quantidade mínima")
    @Column(nullable = false)
    private Integer quantidade;
    
    @Min(value = 0, message = "Nova quantidade mínima")
    @Column(nullable = false)
    private Integer novaQuantidade;
    
    @Column(nullable = true, name = "quantidade_incrementar_estoque")
    private Integer quantidadeIncrementar;
    
    @NotNull(message = "Valor de custo não pode ser null!")
    @Min(value = 0, message = "Valor de custo mínima")
    @Column(nullable = false, name = "valor_custo")
    private BigDecimal valorCusto;
    
    @NotNull(message = "Valor de venda não pode ser null!")
    @Min(value = 0, message = "Valor de venda mínima")
    @Column(nullable = false, name = "valor_venda")
    private BigDecimal valorVenda;
    
    @Transient
    private String codigoBarra;
    
    @Min(value = 0, message = "Valor total")
    @Column(nullable = false, name = "valor_total")
    private BigDecimal valorTotal;
    
    @Min(value = 0, message = "Novo valor de custo")
    @Column(nullable = false, name = "novo_valor_custo")
    private BigDecimal novoValorCusto;
    
    @Min(value = 0, message = "Novo valor de venda")
    @Column(nullable = false, name = "novo_valor_venda")
    private BigDecimal novoValorVenda;
    
    @NotBlank(message = "Forma de pagamento não pode ter espaços em branco!")
    @NotEmpty(message = "Forma de pagamento não pode ser vazio!")
    @NotNull(message = "Forma de pagamento não pode ser null!")
    @Column(length = 255, name = "forma_pagamento", nullable = false)
    private String formaPagamento;

    @NotNull(message = "Situacao da compra não pode ser null!")
    @Column(name = "situacao_compra", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private SituacaoCompra situacaoCompra;
    
    @Version
    @Column(name = "versao_objeto", nullable = false)
    private Integer versaoObjeto;
    
    @PrePersist
    @PreUpdate
    private void removeCaracterEspeciais() {
        this.numeroNota = StringUtils.strip(this.numeroNota);
        this.cnpjCpf = StringUtils.getDigits(this.cnpjCpf);
        this.formaPagamento = StringUtils.strip(this.formaPagamento);
        this.fornecedor = StringUtils.strip(this.fornecedor);
    }
    
}

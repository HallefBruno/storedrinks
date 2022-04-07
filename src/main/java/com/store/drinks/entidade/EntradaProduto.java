package com.store.drinks.entidade;

import com.store.drinks.entidade.enuns.SituacaoCompra;
import com.store.drinks.repository.util.Multitenancy;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;
import com.store.drinks.entidade.enuns.FormaPagamento;

@Data
@Entity
@DynamicUpdate
@Table(name = "entrada_produto")
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
  @Column(length = 30, name = "numero_nota", nullable = false, unique = true)
  private String numeroNota;

  @NotNull(message = "Fornecedor do produto não pode ser null!")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Fornecedor fornecedor;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "data_emissao", nullable = false)
  private LocalDate dataEmissao;

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

  @NotNull(message = "Forma de pagamento é obrigatória!")
  @Column(length = 80, name = "forma_pagamento", nullable = false)
  @Enumerated(EnumType.STRING)
  private FormaPagamento formaPagamento;

  @NotNull(message = "Situacao da compra não pode ser null!")
  @Column(name = "situacao_compra", nullable = false, length = 50)
  @Enumerated(EnumType.STRING)
  private SituacaoCompra situacaoCompra;

  @Column(name = "qtd_parcelas")
  private Integer qtdParcelas;

  @Version
  @Column(name = "versao_objeto", nullable = false)
  private Integer versaoObjeto;

  @Column(nullable = false, updatable = false, length = 50)
  private String tenant;

  @PrePersist
  @PreUpdate
  private void prePersistPreUpdate() {
    this.numeroNota = StringUtils.strip(this.numeroNota);
    this.tenant = new Multitenancy().getTenantValue();
    this.tenant = StringUtils.strip(this.tenant);
  }

}
//@JoinColumn(name = "tenant", referencedColumnName = "tenant", nullable = false, unique = true)
//@ManyToOne
//private ClienteSistema clienteSistema;
//@JoinColumn(table = "cliente_sistema", referencedColumnName = "tenant")
//@NotBlank(message = "Cnpj ou cpf não pode ter espaços em branco!")
//@NotEmpty(message = "Cnpj ou cpf não pode ser vazio!")
//@NotNull(message = "Cnpj ou cpf não pode ser null!")
//@Column(length = 20,name = "cnpjcpf", nullable = false)
//private String cnpjCpf;

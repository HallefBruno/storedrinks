
package com.store.drinks.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
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
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
@NamedQuery(query = "from Produto p where (p.descricaoProduto = ?1 or p.codigoBarra = ?2 or p.codProduto = ?3) and p.tenant = ?4 ", 
name = "find produto")
public class Produto extends ETenant implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;
    
    @NotBlank(message = "Código de barra não pode ter espaços em branco!")
    @NotEmpty(message = "Código de barra não pode ser vazio!")
    @NotNull(message = "Código de barra não pode ser null!")
    @Column(length = 30, nullable = true, name = "codigo_barra")
    private String codigoBarra;
    
    @NotBlank(message = "Código do produto não pode ter espaços em branco!")
    @NotEmpty(message = "Código do produto não pode ser vazio!")
    @NotNull(message = "Código do produto não pode ser null!")
    @Column(length = 30, nullable = true, name = "codigo_produto")
    private String codProduto;
    
    @NotBlank(message = "Descrição do produto não pode ter espaços em branco!")
    @NotEmpty(message = "Descrição do produto não pode ser vazio!")
    @NotNull(message = "Descrição do produto não pode ser null!")
    @Column(length = 255, name = "descricao_produto", nullable = false)
    private String descricaoProduto;
    
    @NotNull(message = "Quantidade do produto não pode ser null!")
    @Min(value = 0, message = "Quantidade mínima")
    @Column(nullable = false)
    private Integer quantidade;
    
    @NotNull(message = "Valor de custo não pode ser null!")
    @Min(value = 0, message = "Valor de custo mínima")
    @Column(nullable = false, name = "valor_venda")
    private BigDecimal valorCusto;
    
    @NotNull(message = "Valor de venda não pode ser null!")
    @Min(value = 0, message = "Valor de venda mínima")
    @Column(nullable = false, name = "valor_custo")
    private BigDecimal valorVenda;
    
    @Column(columnDefinition = "boolean default false")
    private Boolean ativo;
    
    @Version
    @Column(name = "versao_objeto", nullable = false)
    private Integer versaoObjeto;
    
    @Column(nullable = false, updatable = false, length = 20)
    private String tenant;
    
    @PrePersist
    @PreUpdate
    private void prePersistPreUpdate() {
        this.codigoBarra = StringUtils.strip(this.codigoBarra);
        this.descricaoProduto = StringUtils.strip(this.descricaoProduto);
        this.descricaoProduto = this.descricaoProduto.toLowerCase();
        this.codProduto = StringUtils.strip(this.codProduto);
        this.tenant = getTenantValue();
        this.tenant = StringUtils.strip(this.tenant);
        if(Objects.isNull(this.ativo)) {
            this.ativo = Boolean.FALSE;
        }
    }
}
//final Locale brLocale = new Locale("pt", "BR");
//final NumberFormat brFormat = NumberFormat.getCurrencyInstance(brLocale);
//System.out.println(brFormat.format(amount));
//@JoinColumn(name = "tenant", referencedColumnName = "tenant", nullable = false, unique = true)
//@ManyToOne
//@JoinColumn(table = "cliente_sistema", referencedColumnName = "tenant")
//private ClienteSistema clienteSistema;
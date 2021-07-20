
package com.store.drinks.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@DynamicUpdate
public class Produto implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;
    
    @NotBlank(message = "Código de barra não pode ter espaços em branco!")
    @NotEmpty(message = "Código de barra não pode ser vazio!")
    @NotNull(message = "Código de barra não pode ser null!")
    @Column(length = 100, nullable = true, name = "codigo_barra", unique = true)
    private String codigoBarra;
    
    @NotBlank(message = "Código do produto não pode ter espaços em branco!")
    @NotEmpty(message = "Código do produto não pode ser vazio!")
    @NotNull(message = "Código do produto não pode ser null!")
    @Column(length = 100, nullable = true, name = "codigo_produto", unique = true)
    private String codProduto;
    
    @NotBlank(message = "Descrição do produto não pode ter espaços em branco!")
    @NotEmpty(message = "Descrição do produto não pode ser vazio!")
    @NotNull(message = "Descrição do produto não pode ser null!")
    @Column(length = 255, name = "descricao_produto", nullable = false, unique = true)
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
    
    @PrePersist
    @PreUpdate
    private void prePersistPreUpdate() {
        this.codigoBarra = StringUtils.strip(this.codigoBarra);
        this.descricaoProduto = StringUtils.strip(this.descricaoProduto);
        this.codProduto = StringUtils.strip(this.codProduto);
    }
    
}
//final Locale brLocale = new Locale("pt", "BR");
//final NumberFormat brFormat = NumberFormat.getCurrencyInstance(brLocale);
//System.out.println(brFormat.format(amount));
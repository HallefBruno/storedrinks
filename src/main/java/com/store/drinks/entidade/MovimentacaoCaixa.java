
package com.store.drinks.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import lombok.Data;

@Data
@Entity
@Table(name = "movimentacao_caixa")
public class MovimentacaoCaixa implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;
    
    @Min(value = 0, message = "Valor mínimo")
    @Column(name = "valor_abertura_caixa", nullable = false)
    private BigDecimal valorAberturaCaixa;
    
    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;
    
    @Column(name = "data_fechamento", nullable = false)
    private LocalDateTime dataFechamento;
    
    @Min(value = 0, message = "Valor mínimo")
    @Column(name = "valor_fechamento_caixa", nullable = false)
    private BigDecimal valorFechamentoCaixa;
    
    @Min(value = 0, message = "Valor mínimo")
    @Column(name = "somatoria_troco", nullable = false)
    private BigDecimal somatoria_troco;
    
    @Column(name = "caixa_aberto", nullable = false)
    private Boolean caixaAberto;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;
    
}

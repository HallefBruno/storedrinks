
package com.store.drinks.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@Table(name = "open_caixa")
@DynamicUpdate
public class OpenCaixa implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;
    
    @Min(value = 0, message = "Valor mínimo")
    @Column(name = "valor_inicial_troco", nullable = false)
    private BigDecimal valorInicialTroco;
    
    @Column(name = "data_hora_fechamento", nullable = false)
    private LocalDateTime dataHoraFechamento;
    
    @Column(name = "data_hora_abertura", nullable = false)
    private LocalDateTime dataHoraAbertura;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Usuario usuario;
    
    @Column(nullable = false)
    private Boolean aberto;
    
}

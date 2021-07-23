
package com.store.drinks.entidade;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
public class Venda implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;
    
    @OneToMany(mappedBy = "venda", orphanRemoval = true, cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ItensVenda> itensVendas;
    
    @Column(name = "data_hora_venda", nullable = false)
    private LocalDateTime dataHoraVenda;

    @Min(value = 0, message = "Valor mínimo")
    @Column(name = "valor_total_venda", nullable = false)
    private BigDecimal valorTotalVenda;
    
}

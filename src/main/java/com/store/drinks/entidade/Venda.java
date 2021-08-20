
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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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
public class Venda extends ETenant implements Serializable {
    
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
    
    //@JoinColumn(name = "tenant", referencedColumnName = "tenant", nullable = false, unique = true)
    //@ManyToOne
    //private ClienteSistema clienteSistema;
    //@JoinColumn(table = "cliente_sistema", referencedColumnName = "tenant")
    @Column(nullable = false, updatable = false, length = 20)
    private String tenant;
    
    @PrePersist
    @PreUpdate
    private void prePersistPreUpdate() {
        this.tenant = getTenantValue();
        this.tenant = StringUtils.strip(this.tenant);
    }
    
}

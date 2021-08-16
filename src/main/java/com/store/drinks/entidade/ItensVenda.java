
package com.store.drinks.entidade;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "itens_venda")
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class ItensVenda extends ETenant implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Produto produto;
    
    @NotNull(message = "Quantidade do produto não pode ser null!")
    @Min(value = 1, message = "Quantidade mínima")
    @Column(nullable = false)
    private Integer quantidade;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    @JsonBackReference
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

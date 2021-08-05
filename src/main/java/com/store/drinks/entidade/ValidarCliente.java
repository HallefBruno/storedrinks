
package com.store.drinks.entidade;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

@Data
@Entity
@EqualsAndHashCode
public class ValidarCliente implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;
    
    @Column(name = "data_validacao", nullable = false)
    private LocalDateTime dataValidacao;
    
    @Column(name = "cpfCnpj", nullable = false)
    private String cpfCnpj;
    
    @PrePersist
    @PreUpdate
    private void prePersistPreUpdate() {
        this.cpfCnpj = StringUtils.strip(this.cpfCnpj);
        this.cpfCnpj = StringUtils.getDigits(this.cpfCnpj);
    }
    
}
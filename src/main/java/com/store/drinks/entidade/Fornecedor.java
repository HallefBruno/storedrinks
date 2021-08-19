
package com.store.drinks.entidade;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
@NamedQuery(query = "from Fornecedor f where (f.nome = 1? or f.cnpf = 2?) and f.tenant = ?3 ", name = "find fornecedor")
public class Fornecedor extends ETenant implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String cnpf;
    private String nome;
    private String tenant;
    
    @PrePersist
    @PreUpdate
    private void prePersistPreUpdate() {
        this.cnpf = StringUtils.getDigits(this.cnpf);
        this.nome = StringUtils.strip(this.nome);
        if(StringUtils.isBlank(this.tenant)) {
            this.tenant = getTenantValue();
        }
        this.tenant = StringUtils.strip(this.tenant);
    }
}


package com.store.drinks.entidade;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Column(unique = true)
    private String email;

    private String senha;

    @Transient
    private String confirmacaoSenha;

    private Boolean ativo;

    @Size(min = 1, message = "Selecione pelo menos um grupo")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_grupo", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name = "id_grupo"))
    private Set<Grupo> grupos;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @PreUpdate
    private void preUpdate() {
        this.confirmacaoSenha = senha;
    }

}

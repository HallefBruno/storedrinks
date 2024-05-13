package com.store.drinks.entidade;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import lombok.Data;

@Data
@Entity
public class Grupo implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grupo_generator")
  @SequenceGenerator(name="grupo_generator", sequenceName = "grupo_seq", allocationSize = 1, initialValue = 1)
  private Long id;
  private String nome;

  @ManyToMany
  @JoinTable(name = "grupo_permissao", joinColumns = @JoinColumn(name = "id_grupo"), inverseJoinColumns = @JoinColumn(name = "id_permissao"))
  private Set<Permissao> permissoes;

}

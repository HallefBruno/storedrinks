package com.store.drinks.entidade;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode
public class Permissao implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permissao_generator")
  @SequenceGenerator(name="permissao_generator", sequenceName = "permissao_seq", allocationSize = 1, initialValue = 1)
  private Long id;
  private String nome;

}

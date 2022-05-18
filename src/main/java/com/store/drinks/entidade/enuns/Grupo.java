package com.store.drinks.entidade.enuns;

import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum Grupo {
  
  ADMIN("Administrador"),
  SUPER_USER("SuperUser"),
  OPERADOR("Operador");
  private final String value;

  private Grupo(String value) {
    this.value = value;
  }
  
  public static Stream<Grupo> stream() {
    return Stream.of(Grupo.values());
  }
  
}

package com.store.drinks.entidade.enuns;

import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum GrupoEnum {
  
  SUPER_USER("SuperUser"),
  ADMIN("Administrador"),
  OPERADOR("Operador");
  private final String value;

  private GrupoEnum(String value) {
    this.value = value;
  }
  
  public static Stream<GrupoEnum> stream() {
    return Stream.of(GrupoEnum.values());
  }
  
}

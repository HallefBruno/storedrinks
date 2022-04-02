
package com.store.drinks.converter;

import com.store.drinks.entidade.enuns.FormaPagamento;
import org.springframework.core.convert.converter.Converter;

public class ConverterStringInEnumFormaPagamento implements Converter<String, FormaPagamento> {

  @Override
  public FormaPagamento convert(String source) {
    return FormaPagamento.valueOf(source.toUpperCase());
  }

}


package com.store.drinks.converter;

import com.store.drinks.entidade.enuns.TipoPagamento;
import org.springframework.core.convert.converter.Converter;

public class ConverterStringInEnumFormaPagamento implements Converter<String, TipoPagamento> {

  @Override
  public TipoPagamento convert(String source) {
    return TipoPagamento.valueOf(source.toUpperCase());
  }

}

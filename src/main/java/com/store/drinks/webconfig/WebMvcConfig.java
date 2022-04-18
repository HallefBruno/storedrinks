package com.store.drinks.webconfig;

import com.store.drinks.converter.ConverterStringInEnumFormaPagamento;
import com.store.drinks.interceptor.TenantInterceptor;
import com.store.drinks.webconfig.format.BigDecimalFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new TenantInterceptor());
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addFormatter(new BigDecimalFormatter("R$ #,##0.00"));
    registry.addConverter(new ConverterStringInEnumFormaPagamento());
  }
}

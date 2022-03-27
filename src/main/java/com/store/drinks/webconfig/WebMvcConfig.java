package com.store.drinks.webconfig;

import com.store.drinks.interceptor.TenantInterceptor;
import com.store.drinks.webconfig.format.BigDecimalFormatter;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
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
    registry.addFormatter(new BigDecimalFormatter("#,##0.00"));
  }
  
  

//  @Bean
//  public FormattingConversionService mvcConversionService() {
//    DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
//    BigDecimalFormatter bigDecimalFormatter = new BigDecimalFormatter("#,##0.00");
//    conversionService.addFormatterForFieldType(BigDecimal.class, bigDecimalFormatter);
//    BigDecimalFormatter integerFormatter = new BigDecimalFormatter("#,##0");
//    conversionService.addFormatterForFieldType(Integer.class, integerFormatter);
//    DateTimeFormatterRegistrar dateTimeFormatter = new DateTimeFormatterRegistrar();
//    dateTimeFormatter.setDateFormatter(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//    dateTimeFormatter.setTimeFormatter(DateTimeFormatter.ofPattern("HH:mm"));
//    dateTimeFormatter.registerFormatters(conversionService);
//    return conversionService;
//  }
//  
//  @Bean
//  public DomainClassConverter<FormattingConversionService> domainClassConverter() {
//    return new DomainClassConverter<>(mvcConversionService());
//  }

}

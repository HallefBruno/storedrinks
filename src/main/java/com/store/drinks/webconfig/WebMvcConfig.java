package com.store.drinks.webconfig;

import com.store.drinks.interceptor.TenantInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new TenantInterceptor());
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    //registry.addMapping("https://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Portuguese-Brasil.json")
    //.allowedOrigins("http://127.0.0.1:8085");
  }

}

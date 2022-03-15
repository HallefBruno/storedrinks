package com.store.drinks.repository.util;

import com.store.drinks.entidade.enuns.Tenant;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class Multitenancy {

  @Autowired
  private HttpServletRequest request;

  public String getTenantValue() {
    this.request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    return request.getAttribute(Tenant.nome.value()).toString();
  }

}

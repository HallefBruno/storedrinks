
package com.store.drinks.entidade;

import com.store.drinks.entidade.enuns.Tenant;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class ETenant {
    
    @Autowired
    private HttpServletRequest request;

    //@RequestAttribute(value = "tenant")
    public String getTenantValue() {
        this.request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getAttribute(Tenant.nome.value()).toString();
    }
}


package com.store.drinks.entidade;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class TenantValue {
    
    @Autowired
    private HttpServletRequest request;

    //@RequestAttribute(value = "yourAttribute") Object 
    public String getTenantValue() {
        this.request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getAttribute("tenant").toString();
    }
}

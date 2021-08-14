
package com.store.drinks.entidade;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

public class TenantValue {
    
    @Autowired
    private HttpServletRequest request;
    
    public String getTenantValue() {
        return request.getAttribute("tenant").toString();
    }
}


package com.store.drinks.util;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class TenantInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(Objects.nonNull(new Tenant().usuario().getClienteSistema())) {
            request.setAttribute("tenant", new Tenant().usuario().getClienteSistema().getTenant());
            return true;
        }
        return true;
    }

}

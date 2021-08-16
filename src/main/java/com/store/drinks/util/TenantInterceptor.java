
package com.store.drinks.util;

import com.store.drinks.entidade.enuns.Tenant;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.security.UsuarioSistema;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

public class TenantInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken) && authentication != null) {
            Usuario usuario = ((UsuarioSistema) authentication.getPrincipal()).getUsuario();
            if(Objects.nonNull(usuario)) {
                request.setAttribute(Tenant.nome.value(), usuario.getClienteSistema().getTenant());
                return true;
            }
        }
        return true;
    }

}

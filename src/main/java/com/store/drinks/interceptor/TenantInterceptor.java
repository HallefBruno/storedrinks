package com.store.drinks.interceptor;

import com.store.drinks.entidade.enuns.Tenant;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.security.UsuarioSistema;
import java.util.Objects;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
public class TenantInterceptor implements HandlerInterceptor {
  
  @Override
  @SuppressWarnings("null")
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken) && Objects.nonNull(authentication)) {
      if (Objects.nonNull(authentication.getPrincipal())) {
        Usuario usuario = ((UsuarioSistema) authentication.getPrincipal()).getUsuario();
        if (Objects.nonNull(usuario)) {
          if(!usuario.getAtivo()) {
            HttpSession session= request.getSession(false);
            SecurityContextHolder.clearContext();
            if(session != null) {
              session.invalidate();
            }
            for(Cookie cookie : request.getCookies()) {
              cookie.setMaxAge(0);
            }
            response.sendRedirect("/login?invalid-session=true"); 
          }
          request.setAttribute(Tenant.NAME.value(), usuario.getClienteSistema().getTenant());
          return true;
        }
      }
    }
    return true;
  }
}

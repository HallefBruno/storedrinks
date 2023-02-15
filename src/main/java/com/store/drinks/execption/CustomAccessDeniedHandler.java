package com.store.drinks.execption;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  public static final Logger LOG = Logger.getLogger(CustomAccessDeniedHandler.class.getName());
  
  @Value("${server.servlet.context-path}")
  private String context;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exc) throws IOException, ServletException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      LOG.log(Level.WARNING, "User: {0} attempted to access the protected URL: {1}", new Object[]{auth.getName(), request.getRequestURI()});
      if(context.concat("/").equalsIgnoreCase(request.getRequestURI())) {
        response.sendRedirect(request.getContextPath() + "/dash-2"); 
        return;
      }
    }
    response.sendRedirect(request.getContextPath() + "/403");
  }
}

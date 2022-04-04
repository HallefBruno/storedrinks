package com.store.drinks.security;

import com.store.drinks.service.LoginAttemptService;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private LoginAttemptService loginAttemptService;

  @Override
  public void onApplicationEvent(final AuthenticationSuccessEvent authenticationSuccessEvent) {
    final String xfHeader = request.getHeader("X-Forwarded-For");
    if (Objects.isNull(xfHeader)) {
      loginAttemptService.loginSucceeded(request.getRemoteAddr());
    } else {
      loginAttemptService.loginSucceeded(xfHeader.split(",")[0]);
    }
  }
}

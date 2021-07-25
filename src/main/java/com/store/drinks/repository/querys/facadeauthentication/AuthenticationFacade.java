
package com.store.drinks.repository.querys.facadeauthentication;

import com.store.drinks.repository.IAuthenticationFacade;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {
    
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

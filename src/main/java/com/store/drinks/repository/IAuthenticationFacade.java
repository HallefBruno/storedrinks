
package com.store.drinks.repository;

import org.springframework.security.core.Authentication;


public interface IAuthenticationFacade {
    Authentication getAuthentication();
}

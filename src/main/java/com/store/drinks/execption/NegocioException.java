package com.store.drinks.execption;

import org.springframework.orm.jpa.JpaSystemException;

/**
 *
 * @author sud
 */
public class NegocioException extends RuntimeException {

    public NegocioException(String message) {
        super(message);
    }
    
    public NegocioException(Throwable ex) {
        if (ex instanceof JpaSystemException) {
            final String msg = ((JpaSystemException) ex).getMostSpecificCause().getLocalizedMessage();
            throw new NegocioException(msg);
        }
    }

}

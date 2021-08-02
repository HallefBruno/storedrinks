package com.store.drinks.execption;

import javax.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ManipuladorException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String internalError(Exception ex, final Model model, HttpServletRequest request) {
        String servPath = request.getServletPath();
        String redirect = servPath.substring(0,servPath.indexOf("/",2));
        String msg = ((DataIntegrityViolationException) ex).getMostSpecificCause().getMessage();
        model.addAttribute("errorMessage", msg);
        model.addAttribute("path", redirect);
        return "error";
    }
    
    @ExceptionHandler(NegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badRequest(Exception ex, final Model model, HttpServletRequest request) {
        String servPath = request.getServletPath();
        String redirect = servPath.substring(0,servPath.indexOf("/",2));
        String msg = ((DataIntegrityViolationException) ex).getMostSpecificCause().getMessage();
        model.addAttribute("errorMessage", msg);
        model.addAttribute("path", redirect);
        return "error";
    }
    
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String notAutorization(Exception ex, final Model model, HttpServletRequest request) {
        String servPath = request.getServletPath();
        String redirect = servPath.substring(0,servPath.indexOf("/",2));
        String msg = ((DataIntegrityViolationException) ex).getMostSpecificCause().getMessage();
        model.addAttribute("errorMessage", msg);
        model.addAttribute("path", redirect);
        return "error";
    }
    

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Throwable throwable, final Model model) {
        logger.error("Exception during execution of SpringSecurity application", throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

}

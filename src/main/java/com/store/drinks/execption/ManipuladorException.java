package com.store.drinks.execption;

import javax.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ManipuladorException {

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

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String nullPointerException(Model model,HttpServletRequest request) {
        String servPath = request.getServletPath();
        String redirect = servPath.substring(0,servPath.indexOf("/",2));
        model.addAttribute("errorMessage", "Erro grave, por favor entrar em contato com admin do sistema!");
        model.addAttribute("path", redirect);
        return "error";
    }

    

//    @ExceptionHandler(NegocioException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String badRequest(Exception ex, final Model model, HttpServletRequest request) {
//        String servPath = request.getServletPath();
//        String redirect = servPath.substring(0,servPath.indexOf("/",2));
//        String msg = ((DataIntegrityViolationException) ex).getMostSpecificCause().getMessage();
//        model.addAttribute("errorMessage", msg);
//        model.addAttribute("path", redirect);
//        return "error";
//    }
//    
//    @ExceptionHandler(AuthenticationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String notAutorization(Exception ex, final Model model, HttpServletRequest request) {
//        String servPath = request.getServletPath();
//        String redirect = servPath.substring(0,servPath.indexOf("/",2));
//        String msg = ((DataIntegrityViolationException) ex).getMostSpecificCause().getMessage();
//        model.addAttribute("errorMessage", msg);
//        model.addAttribute("path", redirect);
//        return "error";
//    }
//    
//
//    @ExceptionHandler(InternalAuthenticationServiceException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String exception(final Throwable throwable, final Model model) {
//        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
//        model.addAttribute("errorMessage", errorMessage);
//        return "error";
//    }
    
//    @ExceptionHandler(JpaSystemException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String defaultErrorHandler(HttpServletRequest req, Exception ex,Model model) {
//        String servPath = req.getServletPath();
//        String redirect = servPath.substring(0,servPath.indexOf("/",2));
//        String contextPath = req.getContextPath();
//        final String msg = ((JpaSystemException)ex).getMostSpecificCause().getLocalizedMessage();
//        model.addAttribute("errorMessage", msg);
//        model.addAttribute("path", redirect);
//        return "error";
//    }

}

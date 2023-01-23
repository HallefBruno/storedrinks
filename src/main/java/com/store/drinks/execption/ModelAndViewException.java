package com.store.drinks.execption;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ModelAndViewException {

  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String nullPointerException(Exception ex, Model model, HttpServletRequest request) {
    String location = request.getRequestURL().toString();
    model.addAttribute("title",ex.getLocalizedMessage());
    model.addAttribute("timestamp",LocalDateTime.now());
    model.addAttribute("message", ex.getMessage());
    model.addAttribute("location", location);
    model.addAttribute("path", "/");
    model.addAttribute("exception", ex);
    model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    return "Error";
  }
  
  @ExceptionHandler(CaixaAbertoPorUsuarioException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String caixaAbertoPorUsuarioException(Exception ex, Model model, HttpServletRequest request) {
    String location = request.getRequestURL().toString();
    model.addAttribute("title","Se estiver vendo esse erro, entre em contato com o Admin do sistema!");
    model.addAttribute("timestamp",LocalDateTime.now());
    model.addAttribute("message", ex.getMessage());
    model.addAttribute("location", location);
    model.addAttribute("path", "/");
    model.addAttribute("exception", ex);
    model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    return "Error";
  }
  
//  @ExceptionHandler(DataIntegrityViolationException.class)
//  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//  public String internalError(Exception ex, final Model model, HttpServletRequest request) {
//    String location = request.getRequestURL().toString();
//    String msg = ((DataIntegrityViolationException) ex).getMostSpecificCause().getMessage();
//    model.addAttribute("title",msg);
//    model.addAttribute("timestamp",LocalDateTime.now());
//    model.addAttribute("message", ex.getMessage());
//    model.addAttribute("location", location);
//    model.addAttribute("path", "/");
//    model.addAttribute("exception", ex);
//    model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//    return "Error";
//  }
  
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

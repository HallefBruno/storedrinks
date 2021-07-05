/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.store.drinks.execption;

import javax.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author halle
 */
@ControllerAdvice
public class ManipuladorException {

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String storageException(Exception ex, final Model model, HttpServletRequest request) {
        String uri = request.getRequestURI();
        String servPath = request.getServletPath();
        String redirect = servPath.substring(0,servPath.indexOf("/",2));
        String msg = ((DataIntegrityViolationException) ex).getMostSpecificCause().getMessage();
        model.addAttribute("errorMessage", msg);
        model.addAttribute("path", redirect);
        return "error";
    }

}

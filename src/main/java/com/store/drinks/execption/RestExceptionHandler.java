package com.store.drinks.execption;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    ApiError apiError = new ApiError();
    for(FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      apiError.errors.add(new FieldMessage(fieldError.getField(), fieldError.getDefaultMessage()));
    }
    apiError.setStatus(status);
    return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
  }
  
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,WebRequest request) {
    ApiError apiError = new ApiError();
    apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
    return new ResponseEntity(apiError, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    ApiError apiError = new ApiError();
    apiError.setMessage("Por favor, verifique o JSON enviado!");
    apiError.setStatus(status);
    return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
  }
  
  @ExceptionHandler(value = { ResponseStatusException.class })
  protected ResponseEntity<Object> responseStatusException(ResponseStatusException ex, WebRequest request) {
    ApiError apiError = new ApiError();
    apiError.setMessage(ex.getReason());
    apiError.setStatus(ex.getStatus());
    return new ResponseEntity(apiError, ex.getStatus());
  }
  
  @ExceptionHandler(value  = { NegocioException.class })
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
    ApiError apiError = new ApiError();
    apiError.setMessage(ex.getMessage());
    apiError.setStatus(HttpStatus.CONFLICT);
    return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.CONFLICT, request);
  }
  
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> constraintViolationException(ConstraintViolationException ex, WebRequest request) {
   List<FieldMessage> errors = new ArrayList<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      FieldMessage fieldMessage = new FieldMessage(violation.getPropertyPath().toString(),violation.getMessage());
      errors.add(fieldMessage);
    }
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }
  
//  @ExceptionHandler(DataIntegrityViolationException.class)
//  public ResponseEntity<Object> dataIntegrityViolationException(Exception ex, WebRequest request) {
//    ApiError apiError = new ApiError();
//    apiError.setMessage(ex.getMessage());
//    apiError.setStatus(HttpStatus.CONFLICT);
//    return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.CONFLICT, request);
//  }
//  
  
  @Getter
  @Setter
  @ToString
  public class ApiError {

    private HttpStatus status;
    private String message;
    private List<FieldMessage> errors;

    public ApiError(HttpStatus status, String message, List<FieldMessage> errors) {
      super();
      this.status = status;
      this.message = message;
      this.errors = errors;
    }

    public ApiError() {
      if(Objects.isNull(this.errors)) {
        this.errors = new ArrayList<>();
      }
    }
  }
  
  @Getter
  @Setter
  @ToString
  public class FieldMessage {

    private String field;
    private String message;

    public FieldMessage(String field, String message) {
      this.field = field;
      this.message = message;
    }
  }
}

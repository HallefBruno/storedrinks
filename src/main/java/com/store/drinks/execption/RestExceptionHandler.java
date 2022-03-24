package com.store.drinks.execption;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    ApiError apiError = new ApiError();
    for(FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      apiError.errors.add(new FieldMessage(fieldError.getField(), fieldError.getDefaultMessage()));
    }
    apiError.setStatus(status);
    return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
  }
  
  @Data
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
  
  @Data
  public class FieldMessage {

    private String field;
    private String message;

    public FieldMessage(String field, String message) {
      this.field = field;
      this.message = message;
    }
  }
}
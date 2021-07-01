
package com.store.drinks.execption;
public class ExceptionHandler {//extends ResponseEntityExceptionHandler {
    
//    @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
//    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
//        List<String> errors = new ArrayList<>();
//        ApiError apiError;
//        
//        if (ex instanceof NullPointerException) {
//            errors.add("Ocorreu um erro no sistema!\nEntre em contato com o adminstrador.");
//            apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), errors);
//            return handleExceptionInternal(ex,apiError, new HttpHeaders(), apiError.getStatus(),request);
//            
//        } else if (ex instanceof DataIntegrityViolationException) {
//            String msg = ((DataIntegrityViolationException) ex).getMostSpecificCause().getMessage();
//            errors.add(msg);
//            apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), errors);
//            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());            
//        } else if(ex instanceof NotFoundRuntimeException) {
//            errors.add(ex.getLocalizedMessage());
//            apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), errors);
//            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
//            
//        } else if(ex instanceof NonNullRuntimeException) {
//            errors.add(ex.getLocalizedMessage());
//            apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), errors);
//            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
//            
//        } else if(ex instanceof NegocioException) {
//            errors.add(ex.getLocalizedMessage());
//            apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), errors);
//            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
//        }
//        
//        errors.add("Ocorreu um erro no sistema!\nEntre em contato com o adminstrador.");
//        apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), errors);
//        return handleExceptionInternal(ex,apiError, new HttpHeaders(), apiError.getStatus(),request);
//    }
//    
//    @Data
//    public class ApiError {
//
//        private HttpStatus status;
//        private String message;
//        private List<String> errors;
//        
//        public ApiError(HttpStatus status, String message, List<String> errors) {
//            super();
//            this.status = status;
//            this.message = message;
//            this.errors = errors;
//        }
//
//        public ApiError(HttpStatus status, String message, String error) {
//            super();
//            this.status = status;
//            this.message = message;
//            errors = Arrays.asList(error);
//        }
//
//    }
//    
//    @Data
//    public class FieldMessage {
//        
//        private String field;
//        private String message;
//
//        public FieldMessage(String field, String message) {
//            this.field = field;
//            this.message = message;
//        }
//    }
    
}

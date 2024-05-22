package com.harrySpringBoot.expensetrackerapi.exceptions;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.harrySpringBoot.expensetrackerapi.entity.ErrorObject;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    // handling custom exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorObject> handleExpenseNotFoundException(ResourceNotFoundException ex,
            WebRequest request) {

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);

    }
    
    @ExceptionHandler(ItemAlreadyExistsExeception.class)
    public ResponseEntity<ErrorObject> handleItemAlreadyExistsExeception(ItemAlreadyExistsExeception ex,
            WebRequest request) {

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.CONFLICT.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.CONFLICT);

    }
    
    // handling Pre-defined class exception
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorObject> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        
        ErrorObject errorObject = new ErrorObject();
        
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<ErrorObject>(errorObject,HttpStatus.BAD_REQUEST);
        
    }


    // handling Generalized Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> handleGeneralException(Exception ex, WebRequest request) {

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);

    }
    


    // -------------------------------------------------

    @Override
    @Nullable 
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new HashMap<String, Object>();

        body.put("timestamp", new Date());
        body.put("statusCode", HttpStatus.BAD_REQUEST.value());

       List<String> errors =  ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());

       body.put("messages", errors);
        
       return new ResponseEntity<Object>(body, HttpStatus.BAD_REQUEST);
    }
}

package com.clientes.clientes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String,Object>> handlerBadRequestException(BadRequestException ex){

        Map<String,Object> errors = new HashMap<>();

        errors.put("Error:","Solicitud incorrecta");
        errors.put("Message:",ex.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        Map<String,Object> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error->
                errors.put(error.getField(),error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handlerResourceNotFoundException(ResourceNotFoundException ex){

        Map<String,Object> errors = new HashMap<>();

        errors.put("Error:","Recurso no encontrado");
        errors.put("Message:",ex.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }
}

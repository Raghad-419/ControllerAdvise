package com.example.safehands.ControllerAdvise;

import com.example.safehands.ApiResponse.ApiException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class ControllerAdvise {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity ApiException(ApiException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity MethodArgumentNotValidException(MethodArgumentNotValidException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(message);
}

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity DataIntegrityViolationException(DataIntegrityViolationException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(message);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity HttpMessageNotReadableException(HttpMessageNotReadableException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(message);
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity NoResourceFoundException(NoResourceFoundException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(message);
    }



    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity RuntimeException(RuntimeException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(message);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(message);
    }


    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity NumberFormatException(NumberFormatException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(message);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity ConstraintViolationException(ConstraintViolationException e) {
        String message =e.getMessage();
        return ResponseEntity.status(400).body(message);
    }


    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity SQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        String message =e.getMessage();
        return ResponseEntity.status(400).body(message);
    }


    @ExceptionHandler(value = InvalidDataAccessResourceUsageException.class)
    public ResponseEntity InvalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException e) {
        String message =e.getMessage();
        return ResponseEntity.status(400).body(message);
    }



}

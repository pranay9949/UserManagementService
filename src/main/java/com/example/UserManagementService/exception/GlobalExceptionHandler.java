package com.example.UserManagementService.exception;

//import org.hibernate.dialect.function.array.H2ArraySetFunction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String ,String>> handleError(UserNotFoundException ex){
        Map<String,String> mp = new HashMap<>();
        mp.put("Error Message","User Not Found Exception");
        return new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ValidEmailException.class)
    public ResponseEntity<Map<String ,String>> handleErrorEmail(ValidEmailException ex){
        Map<String,String> mp = new HashMap<>();
        mp.put("Error Message",ex.getMessage());
        return new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String ,String>> handleErrorEmail(InvalidPasswordException ex){
        Map<String,String> mp = new HashMap<>();
        mp.put("Error Message",ex.getMessage());
        return new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<Map<String ,String>> handleErrorEmail(EmailExistsException ex){
        Map<String,String> mp = new HashMap<>();
        mp.put("Error Message",ex.getMessage());
        return new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
    }
}

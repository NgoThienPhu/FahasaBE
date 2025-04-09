package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex) {
		ApiResponse<Void> response = new ApiResponse<Void>(ex.getMessage());
        ResponseEntity<ApiResponse<Void>> myResponse = new ResponseEntity<ApiResponse<Void>>(response, ex.getStatusCode());
        return myResponse;
    }
}

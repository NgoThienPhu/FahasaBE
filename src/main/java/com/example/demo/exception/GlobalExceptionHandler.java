package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.ApiResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex) {
		ApiResponseDTO<Void> response = new ApiResponseDTO<Void>(ex.getMessage());
        ResponseEntity<ApiResponseDTO<Void>> myResponse = new ResponseEntity<ApiResponseDTO<Void>>(response, ex.getStatusCode());
        return myResponse;
    }
}

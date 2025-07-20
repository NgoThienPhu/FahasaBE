package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.ApiResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleResponseStatusException(Exception ex) {
		ApiResponseDTO<Void> response = new ApiResponseDTO<Void>(ex.getMessage(), "error");
        ResponseEntity<ApiResponseDTO<Void>> myResponse = new ResponseEntity<ApiResponseDTO<Void>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        return myResponse;
    }
	
	@ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex) {
		ApiResponseDTO<Void> response = new ApiResponseDTO<Void>(ex.getMessage(), "error");
        ResponseEntity<ApiResponseDTO<Void>> myResponse = new ResponseEntity<ApiResponseDTO<Void>>(response, ex.getStatusCode());
        return myResponse;
    }
}

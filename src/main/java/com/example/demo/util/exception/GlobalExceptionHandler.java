package com.example.demo.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.util.response.ApiResponse;
import com.example.demo.util.response.ApiResponseError;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleResponseStatusException(Exception ex) {
		ApiResponse apiResponse = new ApiResponseError(500, ex.getMessage());
		var myResponse = new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		return myResponse;
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<?> handleResponseStatusException(CustomException ex, HttpServletRequest request) {
		ApiResponse apiResponse = new ApiResponseError(ex.getStatus().value(), ex.getMessage(),
				ex.getErrorCode() == null ? ex.getStatus().toString() : ex.getErrorCode(), null,
				request.getRequestURI());
		var myResponse = new ResponseEntity<ApiResponse>(apiResponse, ex.getStatus());
		return myResponse;
	}
}

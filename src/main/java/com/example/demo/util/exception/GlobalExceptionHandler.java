package com.example.demo.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponseErrorDTO;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleResponseStatusException(Exception ex) {
		ApiResponseDTO apiResponse = new ApiResponseErrorDTO(500, ex.getMessage());
		ResponseEntity<ApiResponseDTO> myResponse = new ResponseEntity<ApiResponseDTO>(apiResponse,
				HttpStatus.INTERNAL_SERVER_ERROR);
		return myResponse;
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<?> handleResponseStatusException(CustomException ex, HttpServletRequest request) {
		ApiResponseDTO apiResponse = new ApiResponseErrorDTO(ex.getStatus().value(), ex.getMessage(),
				ex.getErrorCode() == null ? ex.getStatus().toString() : ex.getErrorCode(), null,
				request.getRequestURI());
		ResponseEntity<ApiResponseDTO> myResponse = new ResponseEntity<ApiResponseDTO>(apiResponse, ex.getStatus());
		return myResponse;
	}
}

package com.example.demo.util.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.util.response.ResponseFactory;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	private final ResponseFactory responseFactory;

	public GlobalExceptionHandler(ResponseFactory responseFactory) {
		this.responseFactory = responseFactory;
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleResponseStatusException(Exception ex) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		return responseFactory.error(ex.getMessage(), status);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

		BindingResult result = ex.getBindingResult();

		Map<String, String> errors = new HashMap<>();

		if (result.hasErrors()) {
			result.getFieldErrors().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});
		}

		HttpStatus status = HttpStatus.BAD_REQUEST;
		return responseFactory.error("Dữ liệu không hợp lệ", status, errors, request.getRequestURI());
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<?> handleResponseStatusException(CustomException ex, HttpServletRequest request) {
		HttpStatus status = ex.getStatus();
		return responseFactory.error(ex.getMessage(), status);
	}
}

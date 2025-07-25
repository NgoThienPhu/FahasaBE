package com.example.demo.util.validation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.example.demo.dto.ApiResponseDTO;

public class BindingResultUtil {
	
	public static ResponseEntity<?> handleValidationErrors(BindingResult result, String message) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            ApiResponseDTO<Map<String, String>> response = new ApiResponseDTO<>(message, "error", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
	
}

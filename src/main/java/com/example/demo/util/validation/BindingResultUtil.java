package com.example.demo.util.validation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponseErrorDTO;

public class BindingResultUtil {
	
	public static ResponseEntity<?> handleValidationErrors(BindingResult result, String message, String path) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
           ApiResponseDTO response = new ApiResponseErrorDTO(400, message, "VALIDATION_ERROR", errors, path);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
	
}

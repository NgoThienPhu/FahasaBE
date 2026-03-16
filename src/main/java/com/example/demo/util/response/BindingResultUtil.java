package com.example.demo.util.response;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public class BindingResultUtil {
	
	public static ResponseEntity<?> handleValidationErrors(BindingResult result, String message, String path) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
           var response = new ApiResponseError(400, message, "VALIDATION_ERROR", errors, path);
           return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
	
}

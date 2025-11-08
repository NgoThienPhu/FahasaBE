package com.example.demo.util.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

// Ẩn đi những thuộc tính có giá trị là null
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponseDTO<T>(
	
	Boolean success,
	
    String message,
    
    T data,
    
    Map<String, String> errors,
    
    LocalDateTime timestamp
) {
	public ApiResponseDTO(String message, Boolean success, T data) {
        this(success, message, data, null, LocalDateTime.now());
    }
	
	public ApiResponseDTO(String message, Boolean success, Map<String, String> errors) {
        this(success, message, null, errors, LocalDateTime.now());
    }

    public ApiResponseDTO(String message, Boolean success) {
        this(success, message, null, null, LocalDateTime.now());
    }
}

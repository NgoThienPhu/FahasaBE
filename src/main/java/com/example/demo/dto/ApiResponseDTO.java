package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

// Ẩn đi những thuộc tính có giá trị là null
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponseDTO<T>(
	String status,
    String message,
    T data,
    Map<String, String> errors,
    LocalDateTime timestamp
) {
	public ApiResponseDTO(String message, String status, T data) {
        this(status, message, data, null, LocalDateTime.now());
    }
	
	public ApiResponseDTO(String message, String status, Map<String, String> errors) {
        this(status, message, null, errors, LocalDateTime.now());
    }

    public ApiResponseDTO(String message, String status) {
        this(status, message, null, null, LocalDateTime.now());
    }
}

package com.example.demo.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

// Ẩn đi những thuộc tính có giá trị là null
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
	String status,
    String message,
    T data,
    ErrorDetails error,
    LocalDateTime timestamp
) {
	public ApiResponse(String message, T data) {
        this("success", message, data, null, LocalDateTime.now());
    }

    public ApiResponse(String message, ErrorDetails error) {
        this("error", message, null, error, LocalDateTime.now());
    }
}

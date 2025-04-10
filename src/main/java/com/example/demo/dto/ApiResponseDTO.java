package com.example.demo.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

// Ẩn đi những thuộc tính có giá trị là null
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponseDTO<T>(
	String status,
    String message,
    T data,
    LocalDateTime timestamp
) {
	public ApiResponseDTO(String message, T data) {
        this("success", message, data, LocalDateTime.now());
    }

    public ApiResponseDTO(String message) {
        this("error", message, null, LocalDateTime.now());
    }
}

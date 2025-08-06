package com.example.demo.dto.common;

import java.time.LocalDateTime;
import java.util.Map;

import com.example.demo.utils.view.View;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

// Ẩn đi những thuộc tính có giá trị là null
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponseDTO<T>(
	
	@JsonView(View.Public.class)
	String status,
	
	@JsonView(View.Public.class)
    String message,
    
    @JsonView({View.Public.class, View.Internal.class, View.Admin.class})
    T data,
    
    @JsonView(View.Public.class)
    Map<String, String> errors,
    
    @JsonView(View.Public.class)
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

package com.example.demo.util.response;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

	private T data;
	private String message;
	private Pagination pagination;
	private String errorCode;
	private Map<String, String> errors;
	private String path;
	private LocalDateTime timestamp;
	
	public ApiResponse(String message) {
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}

	public ApiResponse(T data, String message) {
		this(message);
		this.data = data;
	}
	
	public ApiResponse(T data, String message, Pagination pagination) {
		this(message);
		this.data = data;
		this.pagination = pagination;
	}
	
	public ApiResponse(String message, String errorCode) {
		this(message);
		this.errorCode = errorCode;
	}

	public ApiResponse(String message, String errorCode, Map<String, String> errors, String path) {
		this(message, errorCode);
		this.errors = errors;
		this.path = path;
	}
	
}

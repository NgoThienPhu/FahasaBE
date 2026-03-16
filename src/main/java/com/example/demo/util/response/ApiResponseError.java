package com.example.demo.util.response;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseError extends ApiResponse {
	
	private String error;
	private Map<String, String> errors;
	private String path;
	
	public ApiResponseError(int status, String message, String error,
			Map<String, String> errors, String path) {
		super(status, message);
		this.error = error;
		this.errors = errors;
		this.path = path;
	}

	public ApiResponseError(int status, String message) {
		super(status, message);
	}

}

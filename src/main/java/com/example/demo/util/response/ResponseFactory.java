package com.example.demo.util.response;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class ResponseFactory {

	public ResponseEntity<ApiResponse<Void>> success(String message) {
		return ResponseEntity.ok(new ApiResponse<Void>(message));
	}

	public <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
		return ResponseEntity.ok(new ApiResponse<T>(data, message));
	}

	public <T> ResponseEntity<ApiResponse<T>> success(T data, String message, Pagination pagination) {
		return ResponseEntity.ok(new ApiResponse<T>(data, message, pagination));
	}

	public <T> ResponseEntity<ApiResponse<T>> success(T data, String message, HttpStatus status) {
		return ResponseEntity.status(status).body(new ApiResponse<T>(data, message));
	}

	public <T> ResponseEntity<ApiResponse<T>> error(String message, HttpStatus status) {
		return ResponseEntity.status(status).body(new ApiResponse<T>(message, status.getReasonPhrase()));
	}


	public <T> ResponseEntity<ApiResponse<T>> error(String message, HttpStatus status, Map<String, String> errors, String path) {
		return ResponseEntity.status(status).body(new ApiResponse<T>(message, status.getReasonPhrase(), errors, path));
	}

}
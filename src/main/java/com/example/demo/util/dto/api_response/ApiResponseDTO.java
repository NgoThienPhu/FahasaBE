package com.example.demo.util.dto.api_response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ApiResponseDTO {
	
	private int status;
	private String message;
	private LocalDateTime timestamp;
	
	public ApiResponseDTO(int status, String message) {
		this.status = status;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}
	
	

}

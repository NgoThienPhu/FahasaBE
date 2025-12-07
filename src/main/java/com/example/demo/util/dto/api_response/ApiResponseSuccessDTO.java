package com.example.demo.util.dto.api_response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseSuccessDTO <T> extends ApiResponseDTO {
	
	private T data;
	
	public ApiResponseSuccessDTO(int status, String message) {
		super(status, message);
	}


	public ApiResponseSuccessDTO(int status, String message, T data) {
		this(status, message);
		this.data = data;
	}
	
}

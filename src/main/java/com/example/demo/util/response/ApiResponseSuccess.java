package com.example.demo.util.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseSuccess <T> extends ApiResponse {
	
	private T data;
	
	public ApiResponseSuccess(int status, String message) {
		super(status, message);
	}


	public ApiResponseSuccess(int status, String message, T data) {
		this(status, message);
		this.data = data;
	}
	
}

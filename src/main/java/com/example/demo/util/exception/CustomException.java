package com.example.demo.util.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException {
	
    private static final long serialVersionUID = 2456149824136232850L;
    
    private HttpStatus status;
	private String errorCode;
    

    public CustomException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }


	public CustomException(HttpStatus status, String message) {
		super(message);
		this.status = status;
		this.errorCode = status.toString();
	}
    
    
    
}

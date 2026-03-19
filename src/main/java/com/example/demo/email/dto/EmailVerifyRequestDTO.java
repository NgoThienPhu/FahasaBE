package com.example.demo.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailVerifyRequestDTO(
		
	@NotBlank(message = "Email không được để trống")
	@Email
	String email,
	
	@NotBlank(message = "Otp không được để trống")
	String otp
		
) {}

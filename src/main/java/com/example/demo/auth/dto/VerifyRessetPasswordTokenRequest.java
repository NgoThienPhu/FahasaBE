package com.example.demo.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifyRessetPasswordTokenRequest(
		
		@NotBlank(message = "RessetPasswordToken không được để trống!")
		String ressetPasswordToken
		
) {

}

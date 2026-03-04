package com.example.demo.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RessetPasswordRequestDTO(
		
		@NotBlank(message = "RessetPasswordToken không được để trống!")
		String ressetPasswordToken,
		
		@NotBlank(message ="Mật khẩu mới không được để trống!")
		String newPassword
		
) {

}

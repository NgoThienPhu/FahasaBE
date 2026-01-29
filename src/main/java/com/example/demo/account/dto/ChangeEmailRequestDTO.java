package com.example.demo.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangeEmailRequestDTO(
		
		@Email(message = "Email không hợp lệ")
		String newEmail,
		
		@NotBlank(message = "Mật khẩu không được để trống")
		String password
		
) {}

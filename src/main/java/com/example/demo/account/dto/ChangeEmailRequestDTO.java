package com.example.demo.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangeEmailRequestDTO(
		
		@Email(message = "Email không hợp lệ")
		String newEmail,
		
		@NotBlank(message = "Otp không được để trống")
		@Pattern(regexp = "^\\d{6}$", message = "Mã xác minh phải gồm đúng 6 chữ số")
		String otp,
		
		@NotBlank(message = "Mật khẩu không được để trống")
		String password
		
) {}

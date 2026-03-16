package com.example.demo.account.dto;

import com.example.demo.account.validation.annotation.UniqueEmail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangeEmailRequestDTO(
		
		@Email(message = "Email không hợp lệ")
		@UniqueEmail(message = "Email đã tồn tại")
		String newEmail,
		
		@NotBlank(message = "Mật khẩu không được để trống")
		String password
		
) {}

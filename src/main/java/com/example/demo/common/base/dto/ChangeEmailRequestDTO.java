package com.example.demo.common.base.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ChangeEmailRequestDTO(
		
		@Email(message = "Email không hợp lệ")
		String newEmail,
		
		@NotNull(message = "token là trường bắt buộc")
		@Pattern(regexp = "^\\d{6}$", message = "Mã xác minh phải gồm đúng 6 chữ số")
		String token
		
) {}

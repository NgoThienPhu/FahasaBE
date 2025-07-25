package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AdminCreateUserRequestDTO(

		@NotBlank(message = "Tên đăng nhập không được để trống") 
		String username,

		@NotBlank(message = "Họ và tên không được để trống") 
		String fullName,
		
		@NotNull(message = "Số điện thoại không được để trống")
		@Pattern(regexp = "^(0|\\+84)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5]|9[0-9])\\d{7}$", message = "Số điện thoại không hợp lệ")
		String phoneNumber,
		
		@Email(message = "Email không hợp lệ")
		String email

) {}

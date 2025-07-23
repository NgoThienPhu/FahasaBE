package com.example.demo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public record CreateUserRequestDTO(

		@NotBlank(message = "Tên đăng nhập không được để trống") 
		String username,

		@NotBlank(message = "Mật khẩu không được để trống") 
		String password,

		@NotBlank(message = "Họ và tên không được để trống") 
		String fullName,
		
		@Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "Giới tính phải là MALE | FEMALE | OTHER")
		String gender,
		
		@Past(message = "Ngày sinh phải là ngày trong quá khứ")
		LocalDate dateOfBirth,

		@Email(message = "Email không hợp lệ") 
		String email,

		@NotBlank(message = "Số điện thoại không được để trống") 
		String phoneNumber

) {}

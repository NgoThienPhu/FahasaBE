package com.example.demo.account.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public record AdminChangeUserInfoRequestDTO(
	
		String fullName,
		
		@Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "Giới tính phải là MALE | FEMALE | OTHER")
		String gender,
		
		@Past(message = "Ngày sinh phải nằm trong quá khứ")
		LocalDate dateOfBirth,
		
		@Email(message = "Email không hợp lệ")
		String email,

		@Pattern(regexp = "^(0|\\+84)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5]|9[0-9])\\d{7}$", message = "Số điện thoại không hợp lệ")
		String phoneNumber	
		
) {}

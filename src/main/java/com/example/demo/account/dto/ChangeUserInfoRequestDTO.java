package com.example.demo.account.dto;

import java.time.LocalDate;

import com.example.demo.util.enums.Gender;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record ChangeUserInfoRequestDTO(
	
	@Size(min = 1, max = 50, message = "Tên phải từ 1-50 ký tự")
	String fullName,
	
	Gender gender,
	
	@Past(message = "Ngày sinh phải nằm trong quá khứ")
	LocalDate dateOfBirth
		
) {}

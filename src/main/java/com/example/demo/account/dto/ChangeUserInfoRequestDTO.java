package com.example.demo.account.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public record ChangeUserInfoRequestDTO(
	
	@NotBlank(message = "Tên đầy đủ không được để trống")
	String fullName,
	
	@Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "Giới tính phải là MALE | FEMALE | OTHER")
	String gender,
	
	@Past(message = "Ngày sinh phải nằm trong quá khứ")
	LocalDate dateOfBirth
		
) {}

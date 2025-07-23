package com.example.demo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Pattern;

public record ChangeUserInfoRequestDTO(
	
	String fullName,
	
	@Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "Giới tính phải là MALE | FEMALE | OTHER")
	String gender,
	
	LocalDate dateOfBirth
		
) {}

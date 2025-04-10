package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.entities.enums.AccountRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

public record LoginResponseDTO 
(
	String id,
	String username,
	@JsonIgnore
	String password,
	AccountRole role,
	LocalDateTime createdAt,
	LocalDateTime updatedAt,
	String fullName,
	String email,
	String phoneNumber,
	Boolean isActive,
	String accessToken
){}

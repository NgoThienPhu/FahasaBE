package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.entities.enums.AccountType;
import com.example.demo.util.view.View;
import com.fasterxml.jackson.annotation.JsonView;

public record LoginResponseDTO(
		
		@JsonView(View.Public.class) 
		String id,

		@JsonView(View.Public.class) 
		String username,

		@JsonView(View.Public.class) 
		AccountType role,

		@JsonView({View.Internal.class, View.Admin.class }) 
		LocalDateTime createdAt,

		@JsonView({ View.Internal.class, View.Admin.class }) 
		LocalDateTime updatedAt,

		@JsonView(View.Public.class) 
		String fullName,

		@JsonView(View.Public.class) 
		String email,

		@JsonView(View.Public.class) 
		String phoneNumber,

		@JsonView(View.Public.class) 
		Boolean isActive,

		@JsonView(View.Public.class) 
		String accessToken

){}

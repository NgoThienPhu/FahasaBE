package com.example.demo.dto;

import com.example.demo.entities.Email;
import com.example.demo.entities.PhoneNumber;
import com.example.demo.utils.view.View;
import com.fasterxml.jackson.annotation.JsonView;

public record LoginResponseDTO(
		
		@JsonView(View.Self.class) 
		String id,

		@JsonView(View.Self.class) 
		String username,

		@JsonView(View.Self.class) 
		String fullName,

		@JsonView(View.Self.class) 
		Email email,

		@JsonView(View.Self.class) 
		PhoneNumber phoneNumber,

		@JsonView(View.Self.class) 
		String accessToken
		
){}

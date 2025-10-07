package com.example.demo.email.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.base.dto.ApiResponseDTO;
import com.example.demo.email.dto.EmailVerifyRequestDTO;
import com.example.demo.email.service.EmailService;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

	private EmailService emailService;
	
	public EmailController(EmailService emailService) {
		this.emailService = emailService;
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOTP(@RequestBody EmailVerifyRequestDTO body) {
		emailService.verify(body.email(), body.otp());
		var response = new ApiResponseDTO<Void>("Xác thực Email thành công", true);
		return new ResponseEntity<ApiResponseDTO<Void>>(response, HttpStatus.OK);
	}
	
}

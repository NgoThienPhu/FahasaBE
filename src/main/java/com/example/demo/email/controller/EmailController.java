package com.example.demo.email.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.base.dto.ApiResponseDTO;
import com.example.demo.email.application.EmailApplicationService;
import com.example.demo.email.dto.EmailVerifyRequestDTO;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

	private EmailApplicationService emailApplicationService;

	public EmailController(EmailApplicationService emailApplicationService) {
		this.emailApplicationService = emailApplicationService;
	}
	
	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOTP(@RequestBody EmailVerifyRequestDTO body) {
		emailApplicationService.verify(body.email(), body.otp());
		var response = new ApiResponseDTO<Void>("Xác thực Email thành công", true);
		return new ResponseEntity<ApiResponseDTO<Void>>(response, HttpStatus.OK);
	}
	
}

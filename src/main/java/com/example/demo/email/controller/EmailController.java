package com.example.demo.email.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.email.dto.EmailVerifyRequestDTO;
import com.example.demo.email.service.EmailService;
import com.example.demo.util.response.ResponseFactory;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

	private EmailService emailService;
	private ResponseFactory responseFactory;
	
	public EmailController(EmailService emailService, ResponseFactory responseFactory) {
		this.emailService = emailService;
		this.responseFactory = responseFactory;
	}
	
	@GetMapping("/send-otp")
	public ResponseEntity<?> sendOtp(@RequestParam(required = true) String toEmail) {
		emailService.sendOtp(toEmail);
		return responseFactory.success("Gửi mã otp thành công!");
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOTP(@Valid @RequestBody EmailVerifyRequestDTO body) {
		emailService.verify(body.email(), body.otp());
		return responseFactory.success("Xác thực thành công");
	}
	
}
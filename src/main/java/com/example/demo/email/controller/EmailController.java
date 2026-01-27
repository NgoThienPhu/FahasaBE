package com.example.demo.email.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;
import com.example.demo.email.dto.EmailVerifyRequestDTO;
import com.example.demo.email.service.EmailService;
import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponseSuccessDTO;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

	private EmailService emailService;
	
	public EmailController(EmailService emailService) {
		this.emailService = emailService;
	}
	
	@GetMapping("/send-otp")
	public ResponseEntity<?> sendOtp(@RequestParam(required = true) String toEmail) {
		emailService.sendOtp(toEmail);
		var myResponse = new ApiResponseSuccessDTO<Void>(200, "Gửi mã otp thành công!");
		return new ResponseEntity<ApiResponseDTO>(myResponse, HttpStatus.OK);
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOTP(@RequestBody EmailVerifyRequestDTO body) {
		emailService.verify(body.email(), body.otp());
		var response = new ApiResponseSuccessDTO<Void>(200, "Xác thực thành công");
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}
	
}

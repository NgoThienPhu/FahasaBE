package com.example.demo.common.service;

public interface EmailService {

	void sendOtpEmail(String toEmail, String subject, String otpCode);
	
}

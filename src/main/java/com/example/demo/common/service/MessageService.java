package com.example.demo.common.service;

public interface MessageService {

	void sendOtpEmail(String toEmail, String subject, String otpCode);
	
}

package com.example.demo.util.service;

public interface MessageService {

	void sendOtpEmail(String toEmail, String subject, String otpCode);
	
}

package com.example.demo.email.service;

import com.example.demo.email.entity.Email;

public interface EmailService {

	Boolean exists(String email);
	
	Email verify(String email, String otp);
	
}

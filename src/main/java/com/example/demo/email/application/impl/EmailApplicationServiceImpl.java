package com.example.demo.email.application.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.common.service.RedisService;
import com.example.demo.email.application.EmailApplicationService;
import com.example.demo.email.entity.Email;
import com.example.demo.email.service.EmailService;

@Service
public class EmailApplicationServiceImpl implements EmailApplicationService {
	
	private EmailService emailService;
	
	private RedisService redisService;
	
	public EmailApplicationServiceImpl(EmailService emailService, RedisService redisService) {
		this.emailService = emailService;
		this.redisService = redisService;
	}

	@Override
	public Boolean exists(String email) {
		return emailService.exists(email);
	}

	@Override
	public Email verify(String email, String otp) {
		String otpCode = redisService.getValue(String.format("OTP:%s", email));
		if (otpCode == null || !otpCode.equals(otp)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã otp không chính xác vui lòng kiểm tra lại");
		} else {
			redisService.deleteValue(String.format("OTP:%s", email));
		}
		return emailService.verify(email);
	}

}

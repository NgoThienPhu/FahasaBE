package com.example.demo.email.service;

import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.auth.service.AuthenticationService;
import com.example.demo.email.entity.Email;
import com.example.demo.email.repository.EmailRepository;
import com.example.demo.email.service.EmailService;
import com.example.demo.util.service.MessageService;
import com.example.demo.util.service.RedisService;

@Service
public class EmailService {

	private EmailRepository emailRepository;
	private RedisService redisService;
	private MessageService messageService;

	public EmailService(EmailRepository emailRepository, RedisService redisService, MessageService messageService) {
		this.emailRepository = emailRepository;
		this.redisService = redisService;
		this.messageService = messageService;
	}

	public Email verify(String email, String otp) {
		String otpCode = redisService.getValue(String.format("OTP:%s", email));
		if (otpCode == null || !otpCode.equals(otp)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã otp không chính xác vui lòng kiểm tra lại");
		} else {
			redisService.deleteValue(String.format("OTP:%s", email));
		}
		Email myEmail = emailRepository.findByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email không tồn tại"));
		myEmail.verify();
		return emailRepository.save(myEmail);
	}
	
	public void sendOtp(String email) {
		String otp = AuthenticationService.generate6DigitCode();
		redisService.setValue(String.format("OTP:%s", email), otp);
		redisService.expire(String.format("OTP:%s", email), 120, TimeUnit.SECONDS);
		messageService.sendOtpEmail(email, "Mã Xác Thực Email Từ FAHASA", otp);
	}

	public Boolean exists(String email) {
		return emailRepository.existsByEmail(email);
	}

}

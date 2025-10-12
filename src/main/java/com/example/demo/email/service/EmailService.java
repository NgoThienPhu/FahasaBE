package com.example.demo.email.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.email.entity.Email;
import com.example.demo.email.repository.EmailRepository;
import com.example.demo.email.service.EmailService;
import com.example.demo.util.service.RedisService;

@Service
public class EmailService {

	private EmailRepository emailRepository;

	private RedisService redisService;

	public EmailService(EmailRepository emailRepository, RedisService redisService) {
		this.emailRepository = emailRepository;
		this.redisService = redisService;
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

	public Boolean exists(String email) {
		return emailRepository.existsByEmail(email);
	}

}

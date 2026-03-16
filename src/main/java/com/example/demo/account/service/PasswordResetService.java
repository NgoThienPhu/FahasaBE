package com.example.demo.account.service;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.example.demo.account.entity.base.Account;
import com.example.demo.util.enums.TokenType;
import com.example.demo.util.service.JwtService;
import com.example.demo.util.service.MessageService;
import com.example.demo.util.service.RedisService;

@Service
public class PasswordResetService {

	private final JwtService jwtService;
	private final RedisService redisService;
	private final MessageService messageService;

	public PasswordResetService(JwtService jwtService, RedisService redisService, MessageService messageService) {

		this.jwtService = jwtService;
		this.redisService = redisService;
		this.messageService = messageService;
	}

	public void requestReset(Account account) {

		String token = jwtService.createToken(account.getUsername(), TokenType.RESSET_PASSWORD);

		String key = "RESET_PASSWORD_TOKEN:" + account.getUsername();

		redisService.setValue(key, token);
		redisService.expire(key, 5L, TimeUnit.MINUTES);

		messageService.sendRessetPasswordEmail(account.getEmail().getEmail(), "Yêu cầu đặt lại mật khẩu", token);
	}
}

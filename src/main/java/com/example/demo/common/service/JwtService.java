package com.example.demo.common.service;

import java.util.Date;

import com.example.demo.account.entity.base.Account;

public interface JwtService {
	
	public String createToken(String username, Account.TokenType tokenType);
	
	public String extractUsername(String token);
	
	public Date extractExpiration(String token);
	
	public Boolean isTokenExpired(String token);
	
}

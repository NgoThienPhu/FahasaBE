package com.example.demo.services.interfaces;

import java.util.Date;

import com.example.demo.entities.base.Account;

public interface JwtService {
	
	public String createToken(String username, Account.TokenType tokenType);
	
	public String extractUsername(String token);
	
	public Date extractExpiration(String token);
	
	public Boolean isTokenExpired(String token);
	
}

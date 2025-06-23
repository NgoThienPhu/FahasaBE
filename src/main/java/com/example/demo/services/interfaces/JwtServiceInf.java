package com.example.demo.services.interfaces;

import java.util.Date;

import com.example.demo.entities.enums.TokenType;

public interface JwtServiceInf {
	
	public String createToken(String username, TokenType tokenType);
	
	public String extractUsername(String token);
	
	public Date extractExpiration(String token);
	
	public Boolean isTokenExpired(String token);
	
}

package com.example.demo.services.interfaces;

import java.util.Date;

import com.example.demo.entities.enums.DeviceType;
import com.example.demo.entities.enums.TokenType;

public interface JwtServiceInf {
	
	public String createToken(String username, TokenType tokenType, DeviceType deviceType);
	
	public String extractUsername(String username);
	
	public Date extractExpiration(String token);
	
	public Boolean isTokenValid(String token, String username);
}

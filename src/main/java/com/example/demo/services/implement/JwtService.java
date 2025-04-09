package com.example.demo.services.implement;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entities.enums.DeviceType;
import com.example.demo.entities.enums.TokenType;
import com.example.demo.services.interfaces.JwtServiceInf;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService implements JwtServiceInf {
	
	@Value("${jwt.secretKey}")
	private String secretKey;

	@Override
	public String createToken(String username, TokenType tokenType, DeviceType deviceType) {
		Map<String, String> claims = new HashMap<String, String>();
		claims.put("tokenType", tokenType.toString());
		claims.put("deviceType", deviceType.toString());
		return generateToken(claims, username);
	}

	@Override
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	@Override
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	@Override
	public Boolean isTokenValid(String token, String username) {
		String tokenSubject = extractUsername(token);
		return (username.equals(tokenSubject)) && isTokenExpired(tokenSubject);
	}
	
	private Key getSigningKey() {
		byte[] keyBytes = secretKey.getBytes();
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	private Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJwt(token).getBody();
	}
	
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private String generateToken(Map<String, String> claims, String username) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()))
				.signWith(getSigningKey(), SignatureAlgorithm.HS512)
				.compact();
	}

}

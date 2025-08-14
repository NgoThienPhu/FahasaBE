package com.example.demo.common.service.impl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.account.entity.base.Account;
import com.example.demo.common.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {
	
	@Value("${jwt.secretKey}")
	private String SECRET_KEY;
	private long ACCESS_TOKEN_EXPIRATION = 900_000;
	private long REFRESH_TOKEN_EXPIRATION = 604_800_000;

	@Override
	public String createToken(String username, Account.TokenType tokenType) {
		Map<String, String> claims = new HashMap<String, String>();
		claims.put("tokenType", tokenType.toString());
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
	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	private Key getSigningKey() {
		byte[] keyBytes = SECRET_KEY.getBytes();
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	private Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
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
				.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
				.signWith(getSigningKey(), SignatureAlgorithm.HS512)
				.compact();
	}

}

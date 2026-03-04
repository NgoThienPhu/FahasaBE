package com.example.demo.util.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.account.entity.base.Account.TokenType;
import com.example.demo.util.exception.CustomException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${jwt.secretKey}")
	private String SECRET_KEY;

	private static final long ACCESS_TOKEN_EXPIRATION = TimeUnit.MINUTES.toMillis(16);
	private static final long RESSET_PASSWORD_TOKEN_EXPIRATION = TimeUnit.MINUTES.toMillis(5);
	private static final long REFRESH_TOKEN_EXPIRATION = TimeUnit.DAYS.toMillis(7) + TimeUnit.MINUTES.toMillis(1);

	public String createToken(String username, TokenType tokenType) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("tokenType", tokenType.name());
		return generateToken(claims, username, tokenType);
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public TokenType extractTokenType(String token) {
		try {
			String tokenType = extractClaim(token, c -> c.get("tokenType", String.class));
			return TokenType.valueOf(tokenType);
		} catch (Exception e) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "TOKEN_INVALID", "Token không hợp lệ");
		}
	}

	public boolean isTokenExpired(String token) {
		try {
			Date expiration = extractExpiration(token);
			return expiration.before(new Date());
		} catch (ExpiredJwtException e) {
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return true;
		}
	}

	public void validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
		} catch (JwtException | IllegalArgumentException e) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "TOKEN_INVALID", "Token không hợp lệ hoặc đã hết hạn");
		}
	}

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}

	private Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	private <T> T extractClaim(String token, Function<Claims, T> resolver) {
		Claims claims = extractClaims(token);
		return resolver.apply(claims);
	}

	private String generateToken(Map<String, Object> claims, String username, TokenType tokenType) {
		long expiration;

		switch (tokenType) {
		case ACCESS:
			expiration = ACCESS_TOKEN_EXPIRATION;
			break;
		case REFRESH:
			expiration = REFRESH_TOKEN_EXPIRATION;
			break;
		case RESSET_PASSWORD:
			expiration = RESSET_PASSWORD_TOKEN_EXPIRATION;
			break;
		default:
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "TOKEN_TYPE_INVALID",
					"Không xác định được loại token");
		}

		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expiration)).addClaims(claims)
				.signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();
	}
}
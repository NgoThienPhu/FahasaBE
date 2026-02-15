package com.example.demo.auth.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.account.entity.base.Account;
import com.example.demo.account.entity.base.Account.TokenType;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.auth.dto.RefreshAccessTokenResponseDTO;
import com.example.demo.util.cookie.CookieUtil;
import com.example.demo.util.exception.CustomException;
import com.example.demo.util.service.JwtService;
import com.example.demo.util.service.RedisService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthenticationService {

	private RedisService redisService;
	private JwtService jwtService;
	private AuthenticationManager authenticationManager;
	private AccountRepository accountRepository;

	public AuthenticationService(RedisService redisService, JwtService jwtService,
			AuthenticationManager authenticationManager, AccountRepository accountRepository) {
		this.redisService = redisService;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
		this.accountRepository = accountRepository;
	}

	public boolean isAuthenticated(String username, String password) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		return authentication.isAuthenticated();
	}
	
	public void logout(String accountId, HttpServletResponse response) {
		try {
			CookieUtil.deleteCookie(response, "refreshToken", "/");
			redisService.deleteValue(String.format("REFRESH_TOKEN:%s", accountId));
			redisService.deleteValue(String.format("ACCESS_TOKEN:%s", accountId));
			SecurityContextHolder.clearContext();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Đăng xuất thất bại");
		}
	}

	public RefreshAccessTokenResponseDTO refreshAccessToken(HttpServletRequest request) {
		String refreshToken = getRefreshToken(request);
		if (refreshToken == null || jwtService.isTokenExpired(refreshToken)) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_EXPIRED",
					"Refresh token không tồn tại hoặc đã hết hạn");
		} else {
			String username = jwtService.extractUsername(refreshToken);
			
			Account account = accountRepository.findByUsername(username)
					.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));
			
			String newAccessToken = jwtService.createToken(username, TokenType.ACCESS);

			redisService.setValue(String.format("ACCESS_TOKEN:%s", account.getId()), newAccessToken);
			redisService.expire(String.format("ACCESS_TOKEN:%s", account.getId()), 15L, TimeUnit.MINUTES);
			return new RefreshAccessTokenResponseDTO(newAccessToken);
		}
	}

	public static String generate6DigitCode() {
		Random random = new Random();
		int code = 100_000 + random.nextInt(900_000);
		return String.valueOf(code);
	}

	public String issueTokens(Account account, HttpServletResponse response) {
		String accessToken = jwtService.createToken(account.getUsername(), Account.TokenType.ACCESS);
		String refreshToken = jwtService.createToken(account.getUsername(), Account.TokenType.REFRESH);

		CookieUtil.setCookie(response, "refreshToken", refreshToken, 7 * 24 * 60 * 60, "/");

		redisService.setValue(String.format("REFRESH_TOKEN:%s", account.getId()), refreshToken);
		redisService.expire(String.format("REFRESH_TOKEN:%s", account.getId()), 7L, TimeUnit.DAYS);
		redisService.setValue(String.format("ACCESS_TOKEN:%s", account.getId()), accessToken);
		redisService.expire(String.format("ACCESS_TOKEN:%s", account.getId()), 15L, TimeUnit.MINUTES);

		return accessToken;
	}

	private String getRefreshToken(HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("refreshToken".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

}

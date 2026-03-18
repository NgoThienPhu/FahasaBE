package com.example.demo.auth.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.account.entity.AdminAccount;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.entity.base.Account;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.auth.dto.RefreshAccessTokenResponseDTO;
import com.example.demo.util.cookie.CookieUtil;
import com.example.demo.util.enums.AccountType;
import com.example.demo.util.enums.TokenType;
import com.example.demo.util.exception.CustomException;
import com.example.demo.util.service.JwtService;
import com.example.demo.util.service.RedisService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {

	private static final String ACCESS_TOKEN_KEY = "ACCESS_TOKEN:%s";
	private static final String REFRESH_TOKEN_KEY = "REFRESH_TOKEN:%s";
	private static final String RESET_PASSWORD_TOKEN_KEY = "RESET_PASSWORD_TOKEN:%s";

	private final RedisService redisService;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final AccountRepository accountRepository;

	public AuthenticationService(RedisService redisService, JwtService jwtService,
			AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
			AccountRepository accountRepository) {
		this.redisService = redisService;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.accountRepository = accountRepository;
	}

	public boolean isAuthenticated(String username, String password) {
		Authentication auth = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		return auth.isAuthenticated();
	}

	public void logout(String username, HttpServletResponse response) {
		Account account = findAccountByUsername(username);
		if (account instanceof UserAccount) {
			CookieUtil.deleteCookie(response, "refreshTokenUser", "/");
		} else {
			CookieUtil.deleteCookie(response, "refreshTokenAdmin", "/");
		}
		deleteRefreshTokenRedis(username);
		deleteAccessTokenRedis(username);
		SecurityContextHolder.clearContext();
	}

	@Transactional(rollbackOn = Exception.class)
	public RefreshAccessTokenResponseDTO refreshAccessToken(HttpServletRequest request, AccountType accountType) {
		String refreshToken = getRefreshToken(request, accountType);
		validateToken(refreshToken, TokenType.REFRESH);

		String redisToken = getRefreshTokenFromRedis(jwtService.extractUsername(refreshToken));
		if (redisToken == null || !refreshToken.equals(redisToken))
			throw new CustomException(HttpStatus.UNAUTHORIZED, "Token làm mới không hợp lệ hoặc đã hết hạn");

		String username = jwtService.extractUsername(refreshToken);
		String newAccessToken = jwtService.createToken(username, TokenType.ACCESS);

		setAccessTokenRedis(username, newAccessToken);

		return new RefreshAccessTokenResponseDTO(newAccessToken);
	}

	@Transactional(rollbackOn = Exception.class)
	public void ressetPassword(String resetPasswordToken, String newPassword) {
		validateToken(resetPasswordToken, TokenType.RESSET_PASSWORD);

		String username = jwtService.extractUsername(resetPasswordToken);
		String redisToken = getResetPasswordTokenFromRedis(username);

		if (!resetPasswordToken.equals(redisToken))
			throw new CustomException(HttpStatus.UNAUTHORIZED, "Token đặt lại mật khẩu không hợp lệ hoặc đã hết hạn");

		Account account = findAccountByUsername(username);

		account.changePassword(passwordEncoder.encode(newPassword));
		accountRepository.save(account);

		deleteResetPasswordTokenRedis(username);
		deleteAccessTokenRedis(username);
		deleteRefreshTokenRedis(username);

	}

	@Transactional(rollbackOn = Exception.class)
	public boolean verifyRessetPasswordToken(String resetPasswordToken) {
		try {
			String username = jwtService.extractUsername(resetPasswordToken);
			String resetPasswordTokenRedis = getResetPasswordTokenFromRedis(username);

			return validateToken(resetPasswordToken, TokenType.RESSET_PASSWORD)
					&& resetPasswordTokenRedis.equals(resetPasswordToken);
		} catch (Exception e) {
			return false;
		}
	}

	public String issueTokens(Account account, HttpServletResponse response) {
		String username = account.getUsername();

		String accessToken = jwtService.createToken(username, TokenType.ACCESS);
		String refreshToken = jwtService.createToken(username, TokenType.REFRESH);

		String cookieName = (account instanceof AdminAccount) ? "refreshTokenAdmin" : "refreshTokenUser";

		CookieUtil.setCookie(response, cookieName, refreshToken, 7 * 24 * 60 * 60, "/");

		setRefreshTokenRedis(username, refreshToken);
		setAccessTokenRedis(username, accessToken);

		return accessToken;
	}

	public static String generate6DigitCode() {
		return String.valueOf(100_000 + new Random().nextInt(900_000));
	}

	private boolean validateToken(String token, TokenType expectedType) {
		if (token == null || jwtService.isTokenExpired(token))
			return false;

		if (jwtService.extractTokenType(token) != expectedType)
			return false;

		return true;
	}

	private String getRefreshToken(HttpServletRequest request, AccountType accountType) {
		if (request.getCookies() == null)
			return null;

		String expectedCookie = (accountType == AccountType.ADMIN) ? "refreshTokenAdmin" : "refreshTokenUser";

		for (Cookie cookie : request.getCookies()) {
			if (expectedCookie.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

	private Account findAccountByUsername(String username) {
		return accountRepository.findByUsername(username).orElseThrow(
				() -> new CustomException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "Người dùng không tồn tại"));
	}

	private String getRefreshTokenFromRedis(String username) {
		String key = String.format(REFRESH_TOKEN_KEY, username);
		return redisService.getValue(key);
	}

	private String getResetPasswordTokenFromRedis(String username) {
		String key = String.format(RESET_PASSWORD_TOKEN_KEY, username);
		return redisService.getValue(key);
	}

	private void setRefreshTokenRedis(String username, String refreshToken) {
		String key = String.format(REFRESH_TOKEN_KEY, username);
		redisService.setValue(key, refreshToken);
		redisService.expire(key, 7, TimeUnit.DAYS);
	}

	private void setAccessTokenRedis(String username, String accessToken) {
		String key = String.format(ACCESS_TOKEN_KEY, username);
		redisService.setValue(key, accessToken);
		redisService.expire(key, 15, TimeUnit.MINUTES);
	}

	private void deleteAccessTokenRedis(String username) {
		redisService.deleteValue(String.format(ACCESS_TOKEN_KEY, username));
	}

	private void deleteRefreshTokenRedis(String username) {
		redisService.deleteValue(String.format(REFRESH_TOKEN_KEY, username));
	}

	private void deleteResetPasswordTokenRedis(String username) {
		redisService.deleteValue(String.format(RESET_PASSWORD_TOKEN_KEY, username));
	}

}

package com.example.demo.auth.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.account.dto.CreateUserRequestDTO;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.entity.base.Account;
import com.example.demo.account.entity.base.Account.TokenType;
import com.example.demo.account.service.UserAccountService;
import com.example.demo.auth.dto.ChangePasswordRequestDTO;
import com.example.demo.auth.dto.LoginRequestDTO;
import com.example.demo.auth.dto.LoginResponseDTO;
import com.example.demo.auth.dto.RefreshAccessTokenResponseDTO;
import com.example.demo.auth.service.AuthenticationService;
import com.example.demo.util.cookie.CookieUtil;
import com.example.demo.util.entity.CustomUserDetails;
import com.example.demo.util.exception.CustomException;
import com.example.demo.util.service.JwtService;
import com.example.demo.util.service.RedisService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {

	private AuthenticationManager authenticationManager;
	private UserAccountService userAccountService;
	private JwtService jwtService;
	private RedisService redisService;
	private PasswordEncoder passwordEncoder;

	public AuthenticationService(AuthenticationManager authenticationManager, UserAccountService userAccountService,
			JwtService jwtService, RedisService redisService, PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.userAccountService = userAccountService;
		this.jwtService = jwtService;
		this.redisService = redisService;
		this.passwordEncoder = passwordEncoder;
	}

	public LoginResponseDTO login(LoginRequestDTO body, HttpServletResponse response) {
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(body.username(), body.password()));

			if (!authentication.isAuthenticated()) {
				throw new CustomException(HttpStatus.BAD_REQUEST, "Tài khoản hoặc mật khẩu không chính xác");
			}

			Account account = userAccountService.findByUsername(body.username());

			String accessToken = jwtService.createToken(account.getUsername(), Account.TokenType.ACCESS);

			String refreshToken = jwtService.createToken(account.getUsername(), Account.TokenType.REFRESH);

			CookieUtil.setCookie(response, "refreshToken", refreshToken, 7 * 24 * 60 * 60, "/");

			redisService.setValue(String.format("REFRESH_TOKEN:%s", account.getId()), refreshToken);
			redisService.expire(String.format("REFRESH_TOKEN:%s", account.getId()), 7L, TimeUnit.DAYS);
			redisService.setValue(String.format("ACCESS_TOKEN:%s", account.getId()), accessToken);
			redisService.expire(String.format("ACCESS_TOKEN:%s", account.getId()), 15L, TimeUnit.MINUTES);

			return new LoginResponseDTO(accessToken);

		} catch (BadCredentialsException | InternalAuthenticationServiceException | ResponseStatusException e) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Tài khoản hoặc mật khẩu không chính xác");
		}
	}

	@Transactional
	public UserAccount userRegister(CreateUserRequestDTO body) {
		UserAccount userAccount = CreateUserRequestDTO.toUserAccount(body, passwordEncoder);
		return (UserAccount) userAccountService.save(userAccount);
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

	public UserAccount userForgotPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	public void changePassword(ChangePasswordRequestDTO body, CustomUserDetails currentUser) {
		try {
			Account account = userAccountService.findById(currentUser.getId());

			if (account == null || !passwordEncoder.matches(body.oldPassword(), account.getPassword()))
				throw new CustomException(HttpStatus.BAD_REQUEST,
						"Tài khoản không tồn tại hoặc mật khẩu không chính xác");

			String newPassword = passwordEncoder.encode(body.newPassword());
			account.setPassword(newPassword);
			userAccountService.save(account);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

	public RefreshAccessTokenResponseDTO refreshTokenAccess(HttpServletRequest request) {
		String refreshToken = getRefreshToken(request);
		if (refreshToken == null || jwtService.isTokenExpired(refreshToken)) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_EXPIRED",
					"Refresh token không tồn tại hoặc đã hết hạn");
		} else {
			String username = jwtService.extractUsername(refreshToken);
			Account account = userAccountService.findByUsername(username);
			String newAccessToken = jwtService.createToken(username, TokenType.ACCESS);

			redisService.setValue(String.format("ACCESS_TOKEN:%s", account.getId()), newAccessToken);
			redisService.expire(String.format("ACCESS_TOKEN:%s", account.getId()), 15L, TimeUnit.MINUTES);
			return new RefreshAccessTokenResponseDTO(newAccessToken);
		}
	}

	public Map<String, String> validateUniqueUserFields(String username, String email, String phoneNumber) {
		Map<String, String> errors = new HashMap<>();

		if (userAccountService.existsByUsername(username)) {
			errors.put("username", "Tên tài khoản đã được sử dụng");
		}

		if (userAccountService.existsByEmail(email)) {
			errors.put("email", "Email đã được sử dụng");
		}

		if (userAccountService.existsByPhoneNumber(phoneNumber)) {
			errors.put("phoneNumber", "Số điện thoại đã được sử dụng");
		}

		return errors;
	}

	public static String generate6DigitCode() {
		Random random = new Random();
		int code = 100_000 + random.nextInt(900_000);
		return String.valueOf(code);
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

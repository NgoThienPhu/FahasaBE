package com.example.demo.auth.service.impl;

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
import com.example.demo.common.base.entity.CustomUserDetails;
import com.example.demo.common.cookie.CookieUtil;
import com.example.demo.common.service.EmailService;
import com.example.demo.common.service.JwtService;
import com.example.demo.common.service.RedisService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private AuthenticationManager authenticationManager;

	private UserAccountService userAccountService;

	private JwtService jwtService;

	private RedisService redisService;
	
	private EmailService emailService;

	private PasswordEncoder passwordEncoder;

	public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserAccountService userAccountService,
			JwtService jwtService, RedisService redisService, EmailService emailService,
			PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.userAccountService = userAccountService;
		this.jwtService = jwtService;
		this.redisService = redisService;
		this.emailService = emailService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public LoginResponseDTO login(LoginRequestDTO body, HttpServletResponse response) {
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(body.username(), body.password()));

			if (!authentication.isAuthenticated()) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
						"Tài khoản hoặc mật khẩu không chính xác, vui lòng đăng nhập lại");
			}

			Account account = userAccountService.findAccountByUsername(body.username());

			String accessToken = jwtService.createToken(account.getUsername(), Account.TokenType.ACCESS);

			String refreshToken = jwtService.createToken(account.getUsername(), Account.TokenType.REFRESH);

			CookieUtil.setCookie(response, "refreshToken", refreshToken, 7 * 24 * 60 * 60, "/api/auth/refresh");
			
			redisService.setValue(String.format("REFRESH_TOKEN:%s", account.getId()), refreshToken);
			redisService.expire(String.format("REFRESH_TOKEN:%s", account.getId()), 15L, TimeUnit.MINUTES);
			redisService.setValue(String.format("ACCESS_TOKEN:%s", account.getId()), accessToken);
			redisService.expire(String.format("ACCESS_TOKEN:%s", account.getId()), 7L, TimeUnit.DAYS);

			return new LoginResponseDTO(accessToken);

		} catch (BadCredentialsException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"Tài khoản hoặc mật khẩu không chính xác, vui lòng đăng nhập lại");
		} catch (InternalAuthenticationServiceException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"Tài khoản hoặc mật khẩu không chính xác, vui lòng đăng nhập lại");
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Transactional
	@Override
	public UserAccount userRegister(CreateUserRequestDTO body) {
		Boolean checkExistsUsername = userAccountService.existsAccountByUsername(body.username());
		Boolean checkExistsEmail = userAccountService.exitstAccountByEmail(body.email());
		Boolean checkExistsPhoneNumber = userAccountService.existsByPhoneNumber(body.phoneNumber());

		if (checkExistsUsername)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Tên đăng nhập đã tồn tại, vui lòng thử tên đăng nhập khác");
		if (checkExistsEmail)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại, vui lòng thử Email khác");
		if (checkExistsPhoneNumber)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Số điện thoại đã tồn tại, vui lòng thử số điện thoại khác");

		String otpCode = redisService.getValue(String.format("OTP:%s", body.email()));
		if (otpCode == null || !otpCode.equals(body.otpCode())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã otp không chính xác vui lòng kiểm tra lại");
		} else {
			redisService.deleteValue(body.email());
		}

		UserAccount userAccount = CreateUserRequestDTO.toUserAccount(body, passwordEncoder);
		userAccount.activate();
		return (UserAccount) userAccountService.save(userAccount);
	}

	@Override
	public void logout(String accountId, HttpServletResponse response) {
		try {
			CookieUtil.deleteCookie(response, "refresh-token", "/api/auth/refresh");
			redisService.deleteValue(String.format("REFRESH_TOKEN:%s", accountId));
			redisService.deleteValue(String.format("ACCESS_TOKEN:%s", accountId));
			SecurityContextHolder.clearContext();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Đăng xuất thất bại vui lòng thử lại sau!");
		}
	}

	@Override
	public UserAccount userForgotPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changePassword(ChangePasswordRequestDTO body, CustomUserDetails currentUser) {
		try {
			Account account = userAccountService.findAccountById(currentUser.getId());

			if (account == null || !passwordEncoder.matches(body.oldPassword(), account.getPassword()))
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
						"Tài khoản không tồn tại hoặc mật khẩu cũ không chính xác, vui lòng thử lại sau");

			String newPassword = passwordEncoder.encode(body.newPassword());
			account.setPassword(newPassword);
			userAccountService.save(account);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

	@Override
	public RefreshAccessTokenResponseDTO refreshTokenAccess(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = getRefreshToken(request);
		if (refreshToken == null)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"Refresh token không tồn tại hoặc đã hết hạn, vui lòng thử lại sau");
		if (jwtService.isTokenExpired(refreshToken)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"Refresh token không hợp lệ hoặc đã hết hạn, vui lòng thử lại sau");
		} else {
			String username = jwtService.extractUsername(refreshToken);
			String newAccessToken = jwtService.createToken(username, TokenType.ACCESS);
			return new RefreshAccessTokenResponseDTO(newAccessToken);
		}
	}

	private String getRefreshToken(HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("refresh-token".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	@Override
	public void sendOtp(String email) {
		String otp = AuthenticationService.generate6DigitCode();
		redisService.setValue(String.format("OTP:%s", email), otp);
		redisService.expire(String.format("OTP:%s", email), 120L, TimeUnit.SECONDS);
		emailService.sendOtpEmail(email, "🔐 Mã OTP của bạn từ Fahasa", otp);
	}

}

package com.example.demo.auth.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.account.dto.CreateUserRequestDTO;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.entity.base.Account;
import com.example.demo.account.service.UserAccountService;
import com.example.demo.auth.dto.ChangePasswordRequestDTO;
import com.example.demo.auth.dto.LoginRequestDTO;
import com.example.demo.auth.dto.LoginResponseDTO;
import com.example.demo.auth.service.AuthenticationService;
import com.example.demo.common.service.JwtService;

import jakarta.transaction.Transactional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private AuthenticationManager authenticationManager;

	private UserAccountService userAccountService;

	private JwtService jwtService;

	private PasswordEncoder passwordEncoder;

	public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserAccountService userAccountService,
			JwtService jwtService, PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.userAccountService = userAccountService;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public LoginResponseDTO login(LoginRequestDTO body) {
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(body.username(), body.password()));

			if (!authentication.isAuthenticated()) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
						"Tài khoản hoặc mật khẩu không chính xác, vui lòng đăng nhập lại");
			}

			Account account = userAccountService.findAccountByUsername(body.username());

			String accessToken = jwtService.createToken(account.getUsername(), Account.TokenType.ACCESS);

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

		return (UserAccount) userAccountService.save(CreateUserRequestDTO.toUserAccount(body, passwordEncoder));
	}

	@Override
	public void logout() {
		try {
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
	public void changePassword(ChangePasswordRequestDTO body, UserDetails currentUser) {
		try {
			Account account = userAccountService.findAccountByUsername(currentUser.getUsername());

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
	public void tokenRefresh() {
		// TODO Auto-generated method stub

	}

}

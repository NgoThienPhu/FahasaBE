package com.example.demo.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Service;

import com.example.demo.account.entity.AdminAccount;
import com.example.demo.account.repository.AdminAccountRepository;
import com.example.demo.auth.dto.LoginRequestDTO;
import com.example.demo.auth.dto.LoginResponseDTO;
import com.example.demo.util.exception.CustomException;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class AdminAuthenticationService {

	private AuthenticationService authenticationService;
	private AdminAccountRepository adminAccountRepository;

	public AdminAuthenticationService(AuthenticationService authenticationService,
			AdminAccountRepository adminAccountRepository) {
		this.authenticationService = authenticationService;
		this.adminAccountRepository = adminAccountRepository;
	}


	public LoginResponseDTO login(LoginRequestDTO body, HttpServletResponse response) {
		try {
			boolean isAuthenticated = authenticationService.isAuthenticated(body.username(), body.password());

			if (!isAuthenticated)
				throw new CustomException(HttpStatus.BAD_REQUEST, "Tài khoản hoặc mật khẩu không chính xác");

			AdminAccount account = adminAccountRepository.findByUsername(body.username()).orElseThrow(
					() -> new CustomException(HttpStatus.BAD_REQUEST, "Tài khoản hoặc mật khẩu không chính xác"));

			String accessToken = authenticationService.issueTokens(account, response);

			return new LoginResponseDTO(accessToken);

		} catch (BadCredentialsException | InternalAuthenticationServiceException e) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Tài khoản hoặc mật khẩu không chính xác");
		}
	}

}

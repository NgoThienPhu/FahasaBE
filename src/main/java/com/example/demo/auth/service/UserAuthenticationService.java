package com.example.demo.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.account.dto.CreateUserRequestDTO;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.repository.UserAccountRepository;
import com.example.demo.auth.dto.ChangePasswordRequestDTO;
import com.example.demo.auth.dto.LoginRequestDTO;
import com.example.demo.auth.dto.LoginResponseDTO;
import com.example.demo.auth.service.UserAuthenticationService;
import com.example.demo.util.entity.CustomUserDetails;
import com.example.demo.util.exception.CustomException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Service
public class UserAuthenticationService {

	private AuthenticationService authenticationService;
	private PasswordEncoder passwordEncoder;
	private UserAccountRepository userAccountRepository;

	public UserAuthenticationService(AuthenticationService authenticationService, PasswordEncoder passwordEncoder,
			UserAccountRepository userAccountRepository) {
		this.authenticationService = authenticationService;
		this.passwordEncoder = passwordEncoder;
		this.userAccountRepository = userAccountRepository;
	}

	public LoginResponseDTO login(LoginRequestDTO body, HttpServletResponse response) {
		try {
			boolean isAuthenticated = authenticationService.isAuthenticated(body.username(), body.password());

			if (!isAuthenticated)
				throw new CustomException(HttpStatus.BAD_REQUEST, "Tài khoản hoặc mật khẩu không chính xác");

			UserAccount account = findUserAccountByUsername(body.username());

			String accessToken = authenticationService.issueTokens(account, response);

			return new LoginResponseDTO(accessToken);

		} catch (BadCredentialsException | InternalAuthenticationServiceException | ResponseStatusException e) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Tài khoản hoặc mật khẩu không chính xác");
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public UserAccount register(CreateUserRequestDTO body) {
		UserAccount userAccount = CreateUserRequestDTO.toUserAccount(body, passwordEncoder);
		return userAccountRepository.save(userAccount);
	}

	@Transactional(rollbackOn = Exception.class)
	public void changePassword(ChangePasswordRequestDTO body, CustomUserDetails currentUser) {
		UserAccount account = findUserAccountByUsername(currentUser.getUsername());

		if (!passwordEncoder.matches(body.oldPassword(), account.getPassword()))
			throw new CustomException(HttpStatus.UNAUTHORIZED, "Mật khẩu cũ không chính xác");

		String newPassword = passwordEncoder.encode(body.newPassword());
		account.changePassword(newPassword);

		userAccountRepository.save(account);
	}

	private UserAccount findUserAccountByUsername(String username) {
		return userAccountRepository.findByUsername(username)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));
	}

}

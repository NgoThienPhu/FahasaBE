package com.example.demo.auth.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.account.dto.CreateUserRequestDTO;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.repository.AccountRepository;
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
	private AccountRepository accountRepository;

	public UserAuthenticationService(AuthenticationService authenticationService, PasswordEncoder passwordEncoder,
			UserAccountRepository userAccountRepository, AccountRepository accountRepository) {
		this.authenticationService = authenticationService;
		this.passwordEncoder = passwordEncoder;
		this.userAccountRepository = userAccountRepository;
		this.accountRepository = accountRepository;
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

	@Transactional
	public UserAccount register(CreateUserRequestDTO body) {
		UserAccount userAccount = CreateUserRequestDTO.toUserAccount(body, passwordEncoder);
		return userAccountRepository.save(userAccount);
	}

	public UserAccount forgotPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	public void changePassword(ChangePasswordRequestDTO body, CustomUserDetails currentUser) {
			UserAccount account = findUserAccountByUsername(currentUser.getUsername());

			if (!passwordEncoder.matches(body.oldPassword(), account.getPassword()))
				throw new CustomException(HttpStatus.UNAUTHORIZED, "Mật khẩu cũ không chính xác");

			String newPassword = passwordEncoder.encode(body.newPassword());
			account.changePassword(newPassword);

			userAccountRepository.save(account);
	}

	public Map<String, String> validateUniqueUserFields(String username, String email, String phoneNumber) {
		Map<String, String> errors = new HashMap<>();

		if (accountRepository.existsByUsername(username)) {
			errors.put("username", "Tên tài khoản đã được sử dụng");
		}

		if (accountRepository.existsByEmail(email)) {
			errors.put("email", "Email đã được sử dụng");
		}

		if (userAccountRepository.existsByPhoneNumber(phoneNumber)) {
			errors.put("phoneNumber", "Số điện thoại đã được sử dụng");
		}

		return errors;
	}

	private UserAccount findUserAccountByUsername(String username) {
		return userAccountRepository.findByUsername(username)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));
	}

}

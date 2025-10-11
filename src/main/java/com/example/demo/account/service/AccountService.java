package com.example.demo.account.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.account.dto.ChangeEmailRequestDTO;
import com.example.demo.account.entity.base.Account;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.account.service.AccountService;
import com.example.demo.auth.service.AuthenticationService;
import com.example.demo.email.entity.Email;
import com.example.demo.util.service.RedisService;

public class AccountService {

	protected AccountRepository accountRepository;
	protected PasswordEncoder passwordEncoder;
	protected RedisService redisService;

	public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder,
			RedisService redisService) {
		this.accountRepository = accountRepository;
		this.passwordEncoder = passwordEncoder;
		this.redisService = redisService;
	}

	public Account save(Account account) {
		return accountRepository.save(account);
	}

	public Account findById(String accountId) {
		return accountRepository.findById(accountId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				String.format("Không tìm thấy tài khoản với Id là: %s", accountId)));
	}

	public Account findByUsername(String username) {
		return accountRepository.findByUsername(username);
	}

	public Boolean existsByUsername(String username) {
		return accountRepository.existsByUsername(username);
	}

	public Boolean existsByEmail(String email) {
		return accountRepository.existsByEmail(email);
	}

	@Transactional
	public void resetPassword(String accountId) {
		Account account = findById(accountId);
		String password = AuthenticationService.generate6DigitCode();
		account.setPassword(passwordEncoder.encode(password));
		accountRepository.save(account);
		System.out.println(String.format("Mật khẩu mới của bạn là: %s", password));
	}
	
	@Transactional
	public Account changeEmail(ChangeEmailRequestDTO body, String accountId) {
		Account account = findById(accountId);
		if(!passwordEncoder.matches(body.password(), account.getPassword())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu không chính xác");
		}
		if(existsByEmail(body.newEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã được sử dụng");
		}
		String myOtp = redisService.getValue(String.format("OTP:%s", body.newEmail()));
		if(myOtp == null || !body.otp().equals(myOtp)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã Otp không chính xác");
		}
		Email myEmail = new Email(body.newEmail(), true);
		account.setEmail(myEmail);
		return save(account);
	}
	
}

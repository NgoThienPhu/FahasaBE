package com.example.demo.account.service;

import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.account.dto.ChangeEmailRequestDTO;
import com.example.demo.account.entity.base.Account;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.email.entity.Email;
import com.example.demo.util.exception.CustomException;

@Service
public class AccountService {

	private final AccountRepository accountRepository;
	private final PasswordResetService passwordResetService;
	private final AccountValidationService accountValidationService;

	public AccountService(AccountRepository accountRepository, PasswordResetService passwordResetService,
			AccountValidationService accountValidationService) {
		this.accountRepository = accountRepository;
		this.passwordResetService = passwordResetService;
		this.accountValidationService = accountValidationService;
	}

	@Transactional(rollbackOn = Exception.class)
	public Account changeEmail(ChangeEmailRequestDTO dto, String accountId) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));

		accountValidationService.verifyPassword(account, dto.password());

		Email myEmail = new Email(dto.newEmail(), false);
		account.changeEmail(myEmail);

		return accountRepository.save(account);
	}

	@Transactional(rollbackOn = Exception.class)
	public void resetPassword(String accountId) {

		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));

		passwordResetService.requestReset(account);
	}

}

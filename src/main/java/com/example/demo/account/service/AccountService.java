package com.example.demo.account.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.account.dto.AdminCreateUserRequestDTO;
import com.example.demo.account.dto.ChangeEmailRequestDTO;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.entity.base.Account;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.account.repository.UserAccountRepository;
import com.example.demo.auth.service.AuthenticationService;
import com.example.demo.email.entity.Email;
import com.example.demo.util.entity.PhoneNumber;
import com.example.demo.util.exception.CustomException;
import com.example.demo.util.service.MessageService;

@Service
public class AccountService {

	private AccountRepository accountRepository;
	private UserAccountRepository userAccountRepository;
	private PasswordEncoder passwordEncoder;
	private MessageService messageService;

	public AccountService(AccountRepository accountRepository, UserAccountRepository userAccountRepository,
			PasswordEncoder passwordEncoder, MessageService messageService) {
		this.accountRepository = accountRepository;
		this.userAccountRepository = userAccountRepository;
		this.passwordEncoder = passwordEncoder;
		this.messageService = messageService;
	}

	@Transactional
	public UserAccount adminCreateUserAccount(AdminCreateUserRequestDTO dto) {
		if (accountRepository.existsByUsername(dto.username()))
			throw new CustomException(HttpStatus.BAD_REQUEST, "Username đã tồn tại");

		if (accountRepository.existsByEmail(dto.email()))
			throw new CustomException(HttpStatus.BAD_REQUEST, "Email đã tồn tại");

		if (userAccountRepository.existsByPhoneNumber(dto.phoneNumber()))
			throw new CustomException(HttpStatus.BAD_REQUEST, "PhoneNumber đã tồn tại");

		UserAccount user = new UserAccount();
		String password = AuthenticationService.generate6DigitCode();
		String passwordEncode = passwordEncoder.encode(password);
		
		user.initByAdmin(dto.username(), dto.fullName(), new Email(dto.email()), new PhoneNumber(dto.phoneNumber()),
				passwordEncode);

		return userAccountRepository.save(user);
	}

	@Transactional
	public Account changeEmail(ChangeEmailRequestDTO body, String accountId) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));

		if (!passwordEncoder.matches(body.password(), account.getPassword())) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Mật khẩu không chính xác");
		}
		
		if (accountRepository.existsByEmail(body.newEmail())) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Email đã được sử dụng");
		}

		Email myEmail = new Email(body.newEmail(), false);
		account.changeEmail(myEmail);

		return accountRepository.save(account);
	}

	@Transactional
	public void resetPassword(String accountId) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));

		String password = AuthenticationService.generate6DigitCode();
		account.setPassword(passwordEncoder.encode(password));

		accountRepository.save(account);

		messageService.sendResetPasswordEmail(account.getEmail().getEmail(), "Mật khẩu mới của bạn", "Mật khẩu mới của bạn là: " + password);
	}

}

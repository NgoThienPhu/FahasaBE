package com.example.demo.account.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.account.entity.base.Account;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.account.specification.AccountSpecification;
import com.example.demo.auth.service.AuthenticationService;

public abstract class AbstractAccountService<T extends Account> implements AccountService {

	private AccountRepository accountRepository;
	private PasswordEncoder passwordEncoder;

	public AbstractAccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
		this.accountRepository = accountRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Account save(Account account) {
		return accountRepository.save(account);
	}

	@Override
	public Account findAccountById(String accountId) {
		return accountRepository.findById(accountId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				String.format("Không tìm thấy tài khoản với Id là: %s", accountId)));
	}

	@Override
	public Account findAccountByUsername(String username) {
		Specification<Account> spec = AccountSpecification.hasUsername(username);
		return accountRepository.findOne(spec).orElse(null);
	}

	@Override
	public Boolean existsAccountByUsername(String username) {
		Specification<Account> spec = AccountSpecification.hasUsername(username);
		return accountRepository.count(spec) > 0;
	}

	@Override
	public Boolean exitstAccountByEmail(String email) {
		Specification<Account> spec = Specification.where(AccountSpecification.hasEmail(email));
		return accountRepository.count(spec) > 0;
	}

	@Transactional
	@Override
	public void resetPassword(String accountId) {
		Account account = accountRepository.findById(accountId).orElse(null);
		if (account == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
		String password = AuthenticationService.generate6DigitCode();
		account.setPassword(passwordEncoder.encode(password));
		accountRepository.save(account);
		System.out.println(String.format("Mật khẩu mới của bạn là: %s", password));
	}

}

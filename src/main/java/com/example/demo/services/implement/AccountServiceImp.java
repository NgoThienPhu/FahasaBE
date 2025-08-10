package com.example.demo.services.implement;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.entities.common.Account;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.services.interfaces.AccountService;
import com.example.demo.specifications.AccountSpecification;

@Service
public class AccountServiceImp implements AccountService {
	
	private AccountRepository accountRepository;
	
	public AccountServiceImp(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public Account findByUsername(String username) {
		Specification<Account> spec = AccountSpecification.usernameEquals(username);
		return accountRepository.findOne(spec).orElse(null);
	}

	@Override
	public Boolean existsByUsername(String username) {
		Specification<Account> spec = AccountSpecification.usernameEquals(username);
		return accountRepository.count(spec) > 0;
	}

}

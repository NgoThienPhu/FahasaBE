package com.example.demo.services.implement;

import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.entities.account.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.services.interfaces.AccountService;
import com.example.demo.specification.AccountSpecification;

@Service
public class AccountServiceImp implements AccountService {
	
	private AccountRepository accountRepository;
	
	public AccountServiceImp(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public Optional<Account> findByUsername(String username) {
		Specification<Account> spec = AccountSpecification.usernameEquals(username);
		return accountRepository.findOne(spec);
	}

	@Override
	public Boolean existsByUsername(String username) {
		Specification<Account> spec = AccountSpecification.usernameEquals(username);
		return accountRepository.count(spec) > 0;
	}

}

package com.example.demo.account.service;

import com.example.demo.account.entity.base.Account;

public interface AccountService {
	
	Account save(Account account);

	Account findAccountByUsername(String username);

	Account findAccountById(String accountId);
	
	Account changeEmail(String newEmail, String password, String accountId);

	Boolean existsAccountByUsername(String username);

	Boolean exitstAccountByEmail(String email);

	void resetPassword(String accountId);

}

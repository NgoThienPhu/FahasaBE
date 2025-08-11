package com.example.demo.services.interfaces;

import com.example.demo.entities.base.Account;

public interface AccountService {
	
	Account save(Account account);

	Account findAccountByUsername(String username);

	Account findAccountById(String accountId);

	Boolean existsAccountByUsername(String username);

	Boolean exitstAccountByEmail(String email);

	void resetPassword(String accountId);

}

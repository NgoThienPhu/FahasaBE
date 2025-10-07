package com.example.demo.account.service;

import com.example.demo.account.entity.base.Account;

public interface AccountReader {

	Account findById(String accountId);
	
	Account findByUsername(String username);
	
	Boolean existsByUsername(String username);
	
	Boolean existsByEmail(String email);
	
}

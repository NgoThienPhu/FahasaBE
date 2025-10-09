package com.example.demo.account.service;

import com.example.demo.account.dto.ChangeEmailRequestDTO;
import com.example.demo.account.entity.base.Account;

public interface AccountService {

	Account findById(String accountId);

	Account findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	Account save(Account account);

	Account changeEmail(ChangeEmailRequestDTO body, String accountId);

	void resetPassword(String accountId);

}

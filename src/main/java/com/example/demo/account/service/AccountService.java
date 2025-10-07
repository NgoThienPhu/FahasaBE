package com.example.demo.account.service;

import com.example.demo.account.dto.ChangeEmailRequestDTO;
import com.example.demo.account.entity.base.Account;

public interface AccountService extends AccountReader{
	
	Account save(Account account);

	Account changeEmail(ChangeEmailRequestDTO body, String accountId);

	void resetPassword(String accountId);

}

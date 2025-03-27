package com.example.demo.services.interfaces;

import java.util.List;

import com.example.demo.entities.UserAccount;
import com.example.demo.entities.bases.Account;

public interface AccountServiceInf {
	
	public List<Account> getAccounts();
	
	public UserAccount createUserAccount(UserAccount userAccount);
	
	public UserAccount findUserAccountById(String userAccountId);
	
	public List<UserAccount> findUserAccountByUsernameOrEmailOrPhoneNumber(String keyWord);
	
	public UserAccount disableUserAccount(String userAccountId);
	
	public UserAccount activeUserAccount(String userAccountId);
	
}
package com.example.demo.services.interfaces;

import java.util.List;

import com.example.demo.entities.UserAccount;

public interface UserAccountService {
	
	public UserAccount createUserAccount(UserAccount userAccount);
	
	public UserAccount updateUserAccount(UserAccount userAccount);
	
	public Boolean existsByEmail(String email);
	
	public Boolean existsByPhoneNumber(String phoneNumber);
	
	public List<UserAccount> getUserAccounts();
	
	public UserAccount findUserAccountById(String userAccountId);
	
	public UserAccount findUserAccountByUsername(String username);
	
	public List<UserAccount> findUserAccountByUsernameOrEmailOrPhoneNumber(String keyWord);
	
	public UserAccount disableUserAccount(String userAccountId);
	
	public UserAccount activeUserAccount(String userAccountId);
	
	
}
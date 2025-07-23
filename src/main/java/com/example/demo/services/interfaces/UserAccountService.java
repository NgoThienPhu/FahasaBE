package com.example.demo.services.interfaces;

import org.springframework.data.domain.Page;

import com.example.demo.dto.ChangeUserInfoRequestDTO;
import com.example.demo.entities.UserAccount;

public interface UserAccountService {

	UserAccount createUserAccount(UserAccount userAccount);
	
	UserAccount updateUserAccount(UserAccount userAccount);

	UserAccount changeUserInfo(ChangeUserInfoRequestDTO newUserInfo, String userAccountId);

	Boolean existsByEmail(String email);

	Boolean existsByPhoneNumber(String phoneNumber);

	Page<UserAccount> findUserAccounts(String orderBy, String sortBy, int page, int size);

	UserAccount findUserAccountById(String userAccountId);

	UserAccount findUserAccountByUsername(String username);

	Page<UserAccount> findUserAccountByUsernameOrEmailOrPhoneNumber(String keyWord, String orderBy, String sortBy,
			int page, int size);

	UserAccount disableUserAccount(String userAccountId);

	UserAccount activeUserAccount(String userAccountId);

}
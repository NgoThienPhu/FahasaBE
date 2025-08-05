package com.example.demo.services.interfaces;

import org.springframework.data.domain.Page;

import com.example.demo.dto.AdminChangeUserInfoRequestDTO;
import com.example.demo.dto.AdminCreateUserRequestDTO;
import com.example.demo.dto.ChangeUserInfoRequestDTO;
import com.example.demo.entities.UserAccount;

public interface UserAccountService {

	UserAccount createUserAccount(UserAccount userAccount);
	
	UserAccount adminCreateUserAccount(AdminCreateUserRequestDTO dto);

	UserAccount changeUserInfo(ChangeUserInfoRequestDTO newUserInfo, String userAccountId);
	
	UserAccount adminChangeUserInfo(AdminChangeUserInfoRequestDTO newUserInfo, String userAccountId);
	
	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	Boolean existsByPhoneNumber(String phoneNumber);

	Page<UserAccount> findUserAccounts(String orderBy, String sortBy, int page, int size);

	UserAccount findUserAccountById(String userAccountId);

	UserAccount findUserAccountByUsername(String username);

	Page<UserAccount> findUserAccountByUsernameOrEmailOrPhoneNumber(String keyWord, String orderBy, String sortBy,
			int page, int size);

	UserAccount lockUserAccount(String userAccountId);

	UserAccount unlockUserAccount(String userAccountId);
	
	void resetPassword(String userAccountId);
	
	UserAccount save(UserAccount userAccount);
	
}
package com.example.demo.account.service;

import org.springframework.data.domain.Page;

import com.example.demo.account.dto.AdminChangeUserInfoRequestDTO;
import com.example.demo.account.dto.AdminCreateUserRequestDTO;
import com.example.demo.account.dto.ChangeUserInfoRequestDTO;
import com.example.demo.account.entity.UserAccount;

public interface UserAccountService extends AccountService {

	UserAccount adminCreateUserAccount(AdminCreateUserRequestDTO dto);

	UserAccount changeUserAccountInfo(ChangeUserInfoRequestDTO newUserInfo, String userAccountId);
	
	UserAccount adminChangeUserAccountInfo(AdminChangeUserInfoRequestDTO newUserInfo, String userAccountId);
	
	Boolean existsByPhoneNumber(String phoneNumber);

	Page<UserAccount> findUserAccounts(String orderBy, String sortBy, int page, int size);

	Page<UserAccount> findUserAccountByUsernameOrEmailOrPhoneNumber(String keyWord, String orderBy, String sortBy,
			int page, int size);

	UserAccount lockUserAccount(String userAccountId);

	UserAccount unlockUserAccount(String userAccountId);
		
}
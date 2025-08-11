package com.example.demo.services.interfaces;

import org.springframework.data.domain.Page;

import com.example.demo.dto.AdminChangeUserInfoRequestDTO;
import com.example.demo.dto.AdminCreateUserRequestDTO;
import com.example.demo.dto.ChangeUserInfoRequestDTO;
import com.example.demo.entities.account.UserAccount;

public interface UserAccountService {

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
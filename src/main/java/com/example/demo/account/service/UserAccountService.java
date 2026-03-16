package com.example.demo.account.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.account.dto.ChangeEmailRequestDTO;
import com.example.demo.account.dto.ChangeUserInfoRequestDTO;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.repository.UserAccountRepository;
import com.example.demo.util.exception.CustomException;

import jakarta.transaction.Transactional;

@Service
public class UserAccountService {

	private final AccountService accountService;

	private UserAccountRepository userAccountRepository;

	public UserAccountService(UserAccountRepository userAccountRepository, AccountService accountService) {
		this.userAccountRepository = userAccountRepository;
		this.accountService = accountService;
	}

	public UserAccount findById(String accountId) {
		return userAccountRepository.findById(accountId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));
	}

	@Transactional(rollbackOn = Exception.class)
	public UserAccount changeInfo(ChangeUserInfoRequestDTO dto, String userAccountId) {

		UserAccount userAccount = findById(userAccountId);

		if (dto.fullName() != null)
			userAccount.setFullName(dto.fullName());
		if (dto.dateOfBirth() != null)
			userAccount.setDateOfBirth(dto.dateOfBirth());
		if (dto.gender() != null)
			userAccount.setGender(dto.gender());

		return userAccountRepository.save(userAccount);
	}

	@Transactional(rollbackOn = Exception.class)
	public void changeEmail(ChangeEmailRequestDTO dto, String userAccountId) {
		accountService.changeEmail(dto, userAccountId);
	}
}

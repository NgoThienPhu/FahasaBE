package com.example.demo.account.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.account.dto.ChangeEmailRequestDTO;
import com.example.demo.account.dto.ChangeUserInfoRequestDTO;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.repository.UserAccountRepository;
import com.example.demo.util.exception.CustomException;

import jakarta.validation.Valid;

@Service
public class UserAccountService {

    private final AccountService accountService;

	private UserAccountRepository userAccountRepository;
	
	public UserAccountService(UserAccountRepository userAccountRepository, AccountService accountService) {
		this.userAccountRepository = userAccountRepository;
		this.accountService = accountService;
	}

	public UserAccount createAndSave(UserAccount user) {
		return userAccountRepository.save(user);
	}

	public boolean existsByPhoneNumber(String phoneNumber) {
		return userAccountRepository.existsByPhoneNumber(phoneNumber);
	}

	public UserAccount findById(String accountId) {
		return userAccountRepository.findById(accountId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));
	}
	
	public UserAccount findByUsername(String username) {
		return userAccountRepository.findByUsername(username)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));
	} 

	@Transactional
	public UserAccount changeInfo(ChangeUserInfoRequestDTO dto, String userAccountId) {

		UserAccount userAccount = userAccountRepository.findById(userAccountId).orElse(null);

		if (userAccount == null)
			throw new CustomException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
		if (dto.fullName() != null)
			userAccount.setFullName(dto.fullName());
		if (dto.dateOfBirth() != null)
			userAccount.setDateOfBirth(dto.dateOfBirth());
		if (dto.gender() != null)
			userAccount.setGender(UserAccount.Gender.valueOf(dto.gender()));

		return userAccountRepository.save(userAccount);
	}

	public void changeEmail(@Valid ChangeEmailRequestDTO body, String userAccountId) {
		accountService.changeEmail(body, userAccountId);
	}

}

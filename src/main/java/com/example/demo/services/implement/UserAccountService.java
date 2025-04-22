package com.example.demo.services.implement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.services.interfaces.UserAccountServiceInf;

import jakarta.transaction.Transactional;

@Service
public class UserAccountService implements UserAccountServiceInf {
	
	private UserAccountRepository userAccountRepository;

	public UserAccountService(UserAccountRepository userAccountRepository) {
		this.userAccountRepository = userAccountRepository;
	}

	@Override
	public UserAccount findUserAccountById(String userAccountId) {
		return userAccountRepository.findById(userAccountId).orElse(null);
	}

	@Override
	public List<UserAccount> findUserAccountByUsernameOrEmailOrPhoneNumber(String keyWord) {
		return userAccountRepository.searchByKeyword(keyWord);
	}
	
	@Override
	public UserAccount findUserAccountByUsername(String username) {
		return userAccountRepository.findByUsername(username).orElse(null);
	}

	@Transactional
	@Override
	public UserAccount disableUserAccount(String userAccountId) {
		UserAccount user = userAccountRepository.findById(userAccountId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Người dùng không tồn tại"
        ));
		user.setIsActive(false);
		return userAccountRepository.save(user);
	}

	@Transactional
	@Override
	public UserAccount activeUserAccount(String userAccountId) {
		UserAccount user = userAccountRepository.findById(userAccountId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Người dùng không tồn tại"
        ));
		user.setIsActive(true);
		return userAccountRepository.save(user);
	}

	@Override
	public List<UserAccount> getUserAccounts() {
		return userAccountRepository.findAll();
	}

	@Transactional
	@Override
	public UserAccount createUserAccount(UserAccount userAccount) {
		return userAccountRepository.save(userAccount);
	}

	@Override
	public Boolean existsByEmail(String email) {
		return userAccountRepository.existsByEmail(email);
	}

	@Override
	public Boolean existsByPhoneNumber(String phoneNumber) {
		return userAccountRepository.existsByPhoneNumber(phoneNumber);
	}


}

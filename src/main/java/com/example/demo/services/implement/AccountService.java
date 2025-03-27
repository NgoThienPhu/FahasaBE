package com.example.demo.services.implement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.UserAccount;
import com.example.demo.entities.bases.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AdminAccountRepository;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.services.interfaces.AccountServiceInf;

@Service
public class AccountService implements AccountServiceInf {
	
	private AccountRepository accountRepository;
	private UserAccountRepository userAccountRepository;
	private AdminAccountRepository adminAccountRepository;

	public AccountService(AccountRepository accountRepository, UserAccountRepository userAccountRepository,
			AdminAccountRepository adminAccountRepository) {
		this.accountRepository = accountRepository;
		this.userAccountRepository = userAccountRepository;
		this.adminAccountRepository = adminAccountRepository;
	}

	@Override
	public UserAccount createUserAccount(UserAccount userAccount) {
		return userAccountRepository.save(userAccount);
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
	public UserAccount disableUserAccount(String userAccountId) {
		UserAccount user = userAccountRepository.findById(userAccountId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Người dùng không tồn tại"
        ));
		user.setIsActive(false);
		return userAccountRepository.save(user);
	}

	@Override
	public UserAccount activeUserAccount(String userAccountId) {
		UserAccount user = userAccountRepository.findById(userAccountId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Người dùng không tồn tại"
        ));
		user.setIsActive(true);
		return userAccountRepository.save(user);
	}

	@Override
	public List<Account> getAccounts() {
		return accountRepository.findAll();
	}

}

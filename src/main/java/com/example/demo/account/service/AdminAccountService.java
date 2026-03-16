package com.example.demo.account.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.account.entity.AdminAccount;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.repository.AdminAccountRepository;
import com.example.demo.account.repository.UserAccountRepository;
import com.example.demo.util.enums.SortDirection;
import com.example.demo.util.exception.CustomException;
import com.example.demo.util.validation.SortValidator;

import jakarta.transaction.Transactional;

@Service
public class AdminAccountService {

	private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("username", "email", "phoneNumber", "fullName",
			"isActived", "createdAt");

	private final UserAccountRepository userAccountRepository;
	private final AdminAccountRepository adminAccountRepository;
	private final AccountService accountService;

	public AdminAccountService(UserAccountRepository userAccountRepository,
			AdminAccountRepository adminAccountRepository, AccountService accountService) {
		this.userAccountRepository = userAccountRepository;
		this.adminAccountRepository = adminAccountRepository;
		this.accountService = accountService;
	}

	public AdminAccount findAdminAccountById(String adminAccountId) {
		return adminAccountRepository.findById(adminAccountId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Tài khoản quản trị không tồn tại"));
	}

	public UserAccount findUserAccountById(String accountId) {
		return userAccountRepository.findById(accountId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));
	}

	public Page<UserAccount> findUserAccounts(String search, SortDirection orderBy, String sortBy, int page, int size) {

		SortValidator.validate(ALLOWED_SORT_FIELDS, sortBy);

		Sort sort = Sort.by(orderBy == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);

		return userAccountRepository.findByKeyWord(search, pageable);
	}

	@Transactional(rollbackOn = Exception.class)
	public UserAccount lockUserAccount(String userAccountId) {
		UserAccount user = findUserAccountById(userAccountId);

		if (user.getIsActived() == false)
			throw new CustomException(HttpStatus.BAD_REQUEST, "Tài khoản này đã bị khóa");

		user.disabled();

		return userAccountRepository.save(user);
	}

	@Transactional(rollbackOn = Exception.class)
	public UserAccount unlockUserAccount(String userAccountId) {
		UserAccount user = findUserAccountById(userAccountId);

		if (user.getIsActived() == true)
			throw new CustomException(HttpStatus.BAD_REQUEST, "Tài khoản này đã được mở khóa");

		user.active();

		return userAccountRepository.save(user);
	}

	@Transactional(rollbackOn = Exception.class)
	public void resetPassword(String userAccountId) {
		accountService.resetPassword(userAccountId);
	}

}

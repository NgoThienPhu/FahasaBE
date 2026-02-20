package com.example.demo.account.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.account.dto.AdminChangeUserInfoRequestDTO;
import com.example.demo.account.dto.AdminCreateUserRequestDTO;
import com.example.demo.account.entity.AdminAccount;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.repository.AdminAccountRepository;
import com.example.demo.account.repository.UserAccountRepository;
import com.example.demo.util.exception.CustomException;

@Service
public class AdminAccountService {

	private UserAccountRepository userAccountRepository;
	private AdminAccountRepository adminAccountRepository;
	private AccountService accountService;

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

	public UserAccount createUserAccount(AdminCreateUserRequestDTO dto) {
		return accountService.adminCreateUserAccount(dto);
	}

	@Transactional
	public UserAccount changeUserAccountInfo(AdminChangeUserInfoRequestDTO dto, String userAccountId) {

		UserAccount user = findUserAccountById(userAccountId);

		user.updateProfileByAdmin(dto);

		return userAccountRepository.save(user);
	}

	public Page<UserAccount> findUserAccounts(String search, String orderBy, String sortBy, int page, int size) {

		List<String> allowedFields = List.of("username", "email", "phoneNumber", "fullName", "isActived", "createdAt");

		if (!allowedFields.contains(sortBy)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}

		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);

		return userAccountRepository.findByKeyWord(search, pageable);
	}

	@Transactional
	public UserAccount lockUserAccount(String userAccountId) {
		UserAccount user = findUserAccountById(userAccountId);

		if (user.getIsActived() == false)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản này đã bị khóa");

		user.disabled();

		return userAccountRepository.save(user);
	}

	@Transactional
	public UserAccount unlockUserAccount(String userAccountId) {
		UserAccount user = findUserAccountById(userAccountId);

		if (user.getIsActived() == true)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản này đã được mở khóa");

		user.active();

		return userAccountRepository.save(user);
	}

	public void resetPassword(String userAccountId) {
		accountService.resetPassword(userAccountId);
	}

}

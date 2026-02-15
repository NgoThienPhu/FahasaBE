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
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.repository.UserAccountRepository;
import com.example.demo.util.exception.CustomException;

@Service
public class AdminAccountService {

	private UserAccountRepository userAccountRepository;
	private AccountService accountService;

	public AdminAccountService(UserAccountRepository userAccountRepository, AccountService accountService) {
		this.userAccountRepository = userAccountRepository;
		this.accountService = accountService;
	}

	public UserAccount createUserAccount(AdminCreateUserRequestDTO dto) {
		return accountService.adminCreateUserAccount(dto);
	}

	@Transactional
	public UserAccount changeUserAccountInfo(AdminChangeUserInfoRequestDTO dto, String userAccountId) {

		UserAccount user = findById(userAccountId);

		user.updateProfileByAdmin(dto);

		return userAccountRepository.save(user);
	}

	public UserAccount findById(String accountId) {
		return userAccountRepository.findById(accountId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));
	}

	public Page<UserAccount> findUserAccounts(String orderBy, String sortBy, int page, int size) {

		List<String> allowedFields = List.of("username", "email", "phoneNumber");

		if (!allowedFields.contains(sortBy)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}

		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);

		return userAccountRepository.findAll(pageable);
	}

	public Page<UserAccount> findUserAccountByUsernameOrEmailOrPhoneNumber(String keyWord, String orderBy,
			String sortBy, int page, int size) {

		List<String> allowedFields = List.of("username", "email", "phoneNumber");
		if (!allowedFields.contains(sortBy)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}

		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);

		return userAccountRepository.findByUsernameOrEmailOrPhoneNumber(keyWord, pageable);
	}

	@Transactional
	public UserAccount lockUserAccount(String userAccountId) {
		UserAccount user = findById(userAccountId);

		if (user.getIsActived() == false)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản này đã bị khóa");

		user.disabled();

		return userAccountRepository.save(user);
	}

	@Transactional
	public UserAccount unlockUserAccount(String userAccountId) {
		UserAccount user = findById(userAccountId);

		if (user.getIsActived() == true)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản này đã được mở khóa");

		user.active();

		return userAccountRepository.save(user);
	}

	public void resetPassword(String userAccountId) {
		accountService.resetPassword(userAccountId);
	}

}

package com.example.demo.account.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.account.dto.AdminChangeUserInfoRequestDTO;
import com.example.demo.account.dto.AdminCreateUserRequestDTO;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.entity.base.Account;
import com.example.demo.account.service.AccountService;
import com.example.demo.account.service.UserAccountService;
import com.example.demo.common.base.dto.ApiResponseDTO;
import com.example.demo.common.base.dto.PagedResponseDTO;
import com.example.demo.common.validation.BindingResultUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/accounts")
public class AdminAccountController {

	private UserAccountService userAccountService;

	private AccountService accountService;

	public AdminAccountController(UserAccountService userAccountService, AccountService accountService) {
		this.userAccountService = userAccountService;
		this.accountService = accountService;
	}

	@GetMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getUserAccounts(@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "username") String sortBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<UserAccount> accounts = userAccountService.findUserAccounts(orderBy, sortBy, page, size);
		PagedResponseDTO<UserAccount> pagedResponseDTO = PagedResponseDTO.convertPageToPagedResponseDTO(accounts);
		ApiResponseDTO<PagedResponseDTO<UserAccount>> response = new ApiResponseDTO<PagedResponseDTO<UserAccount>>(
				"Lấy danh sách tài khoản thành công", "success", pagedResponseDTO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAccountById(@PathVariable("id") String accountId) {
		Account account = accountService.findAccountById(accountId);
		ApiResponseDTO<? extends Account> response = new ApiResponseDTO<>("Lấy thông tin tài khoản thành công",
				"success", account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createAccount(@Valid @RequestBody AdminCreateUserRequestDTO dto, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Đăng kí thất bại!");
		if (responseError != null)
			return responseError;
		Account account = userAccountService.adminCreateUserAccount(dto);
		ApiResponseDTO<Account> response = new ApiResponseDTO<Account>("Đăng kí thành công", "success", account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/{id}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateProfileUser(@PathVariable("id") String userAccountId,
			@Valid @RequestBody AdminChangeUserInfoRequestDTO dto, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Cập nhật thông thất bại");
		if (responseError != null)
			return responseError;
		UserAccount account = userAccountService.adminChangeUserAccountInfo(dto, userAccountId);
		ApiResponseDTO<Account> response = new ApiResponseDTO<Account>("Cập nhật thông tin thành công", "success",
				account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/{id}/lock")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> lockUserAccount(@PathVariable("id") String userAccountId) {
		UserAccount account = userAccountService.lockUserAccount(userAccountId);
		ApiResponseDTO<Account> response = new ApiResponseDTO<Account>("Đã khóa tài khoản thành công", "success",
				account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/{id}/unlock")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> unlockUserAccount(@PathVariable("id") String userAccountId) {
		UserAccount account = userAccountService.unlockUserAccount(userAccountId);
		ApiResponseDTO<Account> response = new ApiResponseDTO<Account>("Đã mở khóa tài khoản thành công", "success",
				account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/reset-password")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> resetPassword(@PathVariable("id") String userAccountId) {
		accountService.resetPassword(userAccountId);
		ApiResponseDTO<Account> response = new ApiResponseDTO<Account>("Làm mới mật khẩu của tài khoản thành công",
				"success");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}

package com.example.demo.account.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.account.entity.AdminAccount;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.entity.base.Account;
import com.example.demo.account.service.AdminAccountService;
import com.example.demo.util.entity.CustomUserDetails;
import com.example.demo.util.enums.SortDirection;
import com.example.demo.util.response.ApiResponse;
import com.example.demo.util.response.ApiResponsePaginationSuccess;
import com.example.demo.util.response.ApiResponseSuccess;

@RestController
@RequestMapping("/api/admin/accounts")
public class AdminAccountController {

	private AdminAccountService adminAccountService;

	public AdminAccountController(AdminAccountService adminAccountService) {
		this.adminAccountService = adminAccountService;
	}

	@GetMapping("/me")
	public ResponseEntity<?> getInfo(@AuthenticationPrincipal CustomUserDetails currentUser) {
		AdminAccount account = adminAccountService.findAdminAccountById(currentUser.getId());

		var response = new ApiResponseSuccess<Account>(200, "Lấy thông tin tài khoản thành công", account);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> getUserAccounts(@RequestParam(required = false) String search,
			@RequestParam(required = true, defaultValue = "asc") SortDirection orderBy,
			@RequestParam(required = true, defaultValue = "username") String sortBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<UserAccount> accounts = adminAccountService.findUserAccounts(search, orderBy, sortBy, page, size);
		ApiResponsePaginationSuccess<List<UserAccount>> response = ApiResponsePaginationSuccess.fromPage(accounts,
				"Lấy danh sách tài khoản thành công");
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getAccountById(@PathVariable("id") String accountId) {
		UserAccount account = adminAccountService.findUserAccountById(accountId);
		var response = new ApiResponseSuccess<Account>(200, "Lấy thông tin tài khoản thành công", account);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/lock")
	public ResponseEntity<?> lockUserAccount(@PathVariable("id") String userAccountId) {
		UserAccount account = adminAccountService.lockUserAccount(userAccountId);
		var response = new ApiResponseSuccess<UserAccount>(200, "Đã khóa tài khoản thành công", account);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/unlock")
	public ResponseEntity<?> unlockUserAccount(@PathVariable("id") String userAccountId) {
		var account = adminAccountService.unlockUserAccount(userAccountId);
		var response = new ApiResponseSuccess<Account>(200, "Đã mở khóa tài khoản thành công", account);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/reset-password")
	public ResponseEntity<?> resetPassword(@PathVariable("id") String userAccountId) {
		adminAccountService.resetPassword(userAccountId);
		var response = new ApiResponseSuccess<Account>(200, "Yêu cầu đặt lại mật khẩu đã được gửi đến email của người dùng");
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}

}

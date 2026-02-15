package com.example.demo.account.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.example.demo.account.service.AdminAccountService;
import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponsePaginationSuccess;
import com.example.demo.util.dto.api_response.ApiResponseSuccessDTO;
import com.example.demo.util.validation.BindingResultUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/accounts")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAccountController {

	private AdminAccountService adminAccountService;

	public AdminAccountController(AdminAccountService adminAccountService) {
		this.adminAccountService = adminAccountService;
	}

	@GetMapping
	public ResponseEntity<?> getUserAccounts(@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "username") String sortBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<UserAccount> accounts = adminAccountService.findUserAccounts(orderBy, sortBy, page, size);
		var response = new ApiResponsePaginationSuccess<List<UserAccount>>(200, "Lấy danh sách thành công",
				accounts.getContent(), accounts.getNumber(), accounts.getSize(), accounts.getTotalElements(),
				accounts.getTotalPages());
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getAccountById(@PathVariable("id") String accountId) {
		UserAccount account = adminAccountService.findById(accountId);
		var response = new ApiResponseSuccessDTO<Account>(200, "Lấy thông tin tài khoản thành công", account);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> createAccount(@Valid @RequestBody AdminCreateUserRequestDTO dto,
			HttpServletRequest request, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Đăng kí thất bại!",
				request.getRequestURI());
		if (responseError != null)
			return responseError;
		UserAccount account = adminAccountService.createUserAccount(dto);
		var response = new ApiResponseSuccessDTO<UserAccount>(200, "Đăng kí thành công", account);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<?> updateProfileUser(@PathVariable("id") String userAccountId,
			@Valid @RequestBody AdminChangeUserInfoRequestDTO dto, HttpServletRequest request, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Cập nhật thông thất bại",
				request.getRequestURI());
		if (responseError != null)
			return responseError;
		UserAccount account = adminAccountService.changeUserAccountInfo(dto, userAccountId);
		var response = new ApiResponseSuccessDTO<UserAccount>(200, "Cập nhật thông tin thành công", account);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@PatchMapping("/{id}/lock")
	public ResponseEntity<?> lockUserAccount(@PathVariable("id") String userAccountId) {
		UserAccount account = adminAccountService.lockUserAccount(userAccountId);
		var response = new ApiResponseSuccessDTO<UserAccount>(200, "Đã khóa tài khoản thành công", account);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@PatchMapping("/{id}/unlock")
	public ResponseEntity<?> unlockUserAccount(@PathVariable("id") String userAccountId) {
		var account = adminAccountService.unlockUserAccount(userAccountId);
		var response = new ApiResponseSuccessDTO<Account>(200, "Đã mở khóa tài khoản thành công", account);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/reset-password")
	public ResponseEntity<?> resetPassword(@PathVariable("id") String userAccountId) {
		adminAccountService.resetPassword(userAccountId);
		var response = new ApiResponseSuccessDTO<Account>(200, "Làm mới mật khẩu của tài khoản thành công");
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

}

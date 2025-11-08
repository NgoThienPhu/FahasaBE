package com.example.demo.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.account.dto.ChangeEmailRequestDTO;
import com.example.demo.account.dto.ChangePhoneNumberRequestDTO;
import com.example.demo.account.dto.ChangeUserInfoRequestDTO;
import com.example.demo.account.entity.base.Account;
import com.example.demo.account.service.UserAccountService;
import com.example.demo.util.dto.ApiResponseDTO;
import com.example.demo.util.entity.CustomUserDetails;
import com.example.demo.util.validation.BindingResultUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts/me")
public class AccountController {

	private UserAccountService userAccountService;

	public AccountController(UserAccountService userAccountService) {
		this.userAccountService = userAccountService;
	}

	@GetMapping
	public ResponseEntity<?> getInfo(@AuthenticationPrincipal CustomUserDetails currentUser) {
		Account account = userAccountService.findById(currentUser.getId());
		if (account == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
		var response = new ApiResponseDTO<Account>("Lấy thông tin tài khoản thành công", true, account);
		return new ResponseEntity<ApiResponseDTO<Account>>(response, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<?> changeInfo(@RequestBody @Valid ChangeUserInfoRequestDTO dto, BindingResult result,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Cập nhật thất bại!");
		if (responseError != null)
			return responseError;
		Account account = userAccountService.changeUserAccountInfo(dto, currentUser.getId());
		var response = new ApiResponseDTO<Account>("Cập nhật thông tin tài khoản thành công", true, account);
		return new ResponseEntity<ApiResponseDTO<Account>>(response, HttpStatus.OK);
	}

	@PostMapping("/change-email")
	public ResponseEntity<?> changeEmail(@RequestBody @Valid ChangeEmailRequestDTO body, BindingResult result,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Cập nhật email thất bại!");
		if (responseError != null)
			return responseError;
		userAccountService.changeEmail(body, currentUser.getId());
		var response = new ApiResponseDTO<Void>("Đổi email thành công", true);
		return new ResponseEntity<ApiResponseDTO<Void>>(response, HttpStatus.OK);
	}

	@PostMapping("/change-phone")
	public ResponseEntity<?> changePhone(@RequestBody ChangePhoneNumberRequestDTO dto,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		return null;
	}

}

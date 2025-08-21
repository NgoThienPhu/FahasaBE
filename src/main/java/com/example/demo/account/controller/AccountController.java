package com.example.demo.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.account.dto.ChangeEmailRequestDTO;
import com.example.demo.account.dto.ChangePhoneNumberRequestDTO;
import com.example.demo.account.dto.ChangeUserInfoRequestDTO;
import com.example.demo.account.entity.base.Account;
import com.example.demo.account.service.UserAccountService;
import com.example.demo.common.base.dto.ApiResponseDTO;

@RestController
@RequestMapping("/api/accounts/me")
public class AccountController {

	private UserAccountService userAccountService;

	public AccountController(UserAccountService userAccountService) {
		this.userAccountService = userAccountService;
	}

	@GetMapping
	public ResponseEntity<?> getInfo(@AuthenticationPrincipal UserDetails currentUser) {
		Account account = userAccountService.findAccountByUsername(currentUser.getUsername());
		if (account == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Người dùng không tồn tại", currentUser.getUsername()));
		var response = new ApiResponseDTO<Account>("Lấy thông tin tài khoản thành công", "success", account);
		return new ResponseEntity<ApiResponseDTO<Account>>(response, HttpStatus.OK);
	}

	@PatchMapping
	public ResponseEntity<?> changeInfo(@RequestBody ChangeUserInfoRequestDTO dto,
			@AuthenticationPrincipal UserDetails currentUser) {
		Account account = userAccountService.changeUserAccountInfo(dto, currentUser.getUsername());
		var response = new ApiResponseDTO<Account>("Cập nhật thông tin tài khoản thành công", "success", account);
		return new ResponseEntity<ApiResponseDTO<Account>>(response, HttpStatus.OK);
	}

	@PatchMapping("/change-email")
	public ResponseEntity<?> changeEmail(@RequestBody ChangeEmailRequestDTO dto,
			@AuthenticationPrincipal UserDetails currentUser) {
		return null;
	}

	@PatchMapping("/change-phone")
	public ResponseEntity<?> changeEmail(@RequestBody ChangePhoneNumberRequestDTO dto,
			@AuthenticationPrincipal UserDetails currentUser) {
		return null;
	}

}

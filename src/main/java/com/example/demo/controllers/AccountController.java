package com.example.demo.controllers;

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

import com.example.demo.dto.ChangeEmailRequestDTO;
import com.example.demo.dto.ChangePhoneNumberRequestDTO;
import com.example.demo.dto.ChangeUserInfoRequestDTO;
import com.example.demo.dto.common.ApiResponseDTO;
import com.example.demo.entities.account.UserAccount;
import com.example.demo.entities.common.Account;
import com.example.demo.services.interfaces.UserAccountService;
import com.example.demo.utils.view.View;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private UserAccountService userAccountService;

	public AccountController(UserAccountService userAccountService) {
		this.userAccountService = userAccountService;
	}

	@GetMapping("/me")
	@JsonView(View.Self.class)
	public ResponseEntity<?> getInfo(@AuthenticationPrincipal UserDetails currentUser) {
		UserAccount account = userAccountService.findUserAccountByUsername(currentUser.getUsername());
		if (account == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Người dùng không tồn tại", currentUser.getUsername()));
		ApiResponseDTO<Account> response = new ApiResponseDTO<Account>("Lấy thông tin tài khoản thành công", "success",
				account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PatchMapping("/me")
	@JsonView(View.Self.class)
	public ResponseEntity<?> changeInfo(@RequestBody ChangeUserInfoRequestDTO dto, @AuthenticationPrincipal UserDetails currentUser) {
		UserAccount account = userAccountService.changeUserInfo(dto, currentUser.getUsername());
		ApiResponseDTO<Account> response = new ApiResponseDTO<Account>("Cập nhật thông tin tài khoản thành công", "success",
				account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PatchMapping("/me/change-email")
	@JsonView(View.Self.class)
	public ResponseEntity<?> changeEmail(@RequestBody ChangeEmailRequestDTO dto, @AuthenticationPrincipal UserDetails currentUser) {
		return null;
	}
	
	@PatchMapping("/me/change-phone")
	@JsonView(View.Self.class)
	public ResponseEntity<?> changeEmail(@RequestBody ChangePhoneNumberRequestDTO dto, @AuthenticationPrincipal UserDetails currentUser) {
		return null;
	}

}

package com.example.demo.account.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.account.dto.ChangeEmailRequestDTO;
import com.example.demo.account.dto.ChangeUserInfoRequestDTO;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.service.UserAccountService;
import com.example.demo.util.entity.CustomUserDetails;
import com.example.demo.util.response.ResponseFactory;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class UserAccountController {

	private UserAccountService userAccountService;
	private ResponseFactory responseFactory;

	public UserAccountController(UserAccountService userAccountService, ResponseFactory responseFactory) {
		this.userAccountService = userAccountService;
		this.responseFactory = responseFactory;
	}

	@GetMapping("/me")
	public ResponseEntity<?> getAccountPofile(@AuthenticationPrincipal CustomUserDetails currentUser) {
		UserAccount account = userAccountService.findById(currentUser.getId());
		return responseFactory.success(account, "Lấy thông tin tài khoản thành công");
	}

	@PutMapping("me/change-email")
	public ResponseEntity<?> changeEmail(@Valid @RequestBody ChangeEmailRequestDTO dto,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		userAccountService.changeEmail(dto, currentUser.getId());
		return responseFactory.success("Đổi email thành công");
	}

	@PutMapping("/me")
	public ResponseEntity<?> updateAccount(@Valid @RequestBody ChangeUserInfoRequestDTO body,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		UserAccount account = userAccountService.changeInfo(body, currentUser.getId());
		return responseFactory.success(account, "Cập nhật thông tin tài khoản thành công");
	}

}
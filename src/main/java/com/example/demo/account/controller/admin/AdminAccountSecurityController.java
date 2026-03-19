package com.example.demo.account.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.service.AdminAccountService;
import com.example.demo.util.response.ResponseFactory;

@RestController
@RequestMapping("/api/admin/accounts/{accountId}")
public class AdminAccountSecurityController {

	private AdminAccountService adminAccountService;
	private ResponseFactory responseFactory;

	public AdminAccountSecurityController(AdminAccountService adminAccountService, ResponseFactory responseFactory) {
		this.adminAccountService = adminAccountService;
		this.responseFactory = responseFactory;
	}

	@PostMapping("/lock")
	public ResponseEntity<?> lockUserAccount(@PathVariable("accountId") String userAccountId) {
		UserAccount account = adminAccountService.lockUserAccount(userAccountId);
		return responseFactory.success(account, "Đã khóa tài khoản thành công");
	}

	@PostMapping("/unlock")
	public ResponseEntity<?> unlockUserAccount(@PathVariable("accountId") String userAccountId) {
		var account = adminAccountService.unlockUserAccount(userAccountId);
		return responseFactory.success(account, "Đã mở khóa tài khoản thành công");
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@PathVariable("accountId") String userAccountId) {
		adminAccountService.resetPassword(userAccountId);
		return responseFactory.success("Yêu cầu đặt lại mật khẩu đã được gửi đến email của người dùng");
	}

}
package com.example.demo.account.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.account.entity.AdminAccount;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.entity.base.Account;
import com.example.demo.account.service.AdminAccountService;
import com.example.demo.util.entity.CustomUserDetails;
import com.example.demo.util.enums.SortDirection;
import com.example.demo.util.response.ResponseFactory;
import com.example.demo.util.response.Pagination;

@RestController
@RequestMapping("/api/admin/accounts")
public class AdminAccountController {

	private AdminAccountService adminAccountService;
	private ResponseFactory responseFactory;

	public AdminAccountController(AdminAccountService adminAccountService, ResponseFactory responseFactory) {
		this.adminAccountService = adminAccountService;
		this.responseFactory = responseFactory;
	}

	@GetMapping("/me")
	public ResponseEntity<?> getAccountPofile(@AuthenticationPrincipal CustomUserDetails currentUser) {
		AdminAccount account = adminAccountService.findAdminAccountById(currentUser.getId());
		return responseFactory.success(account, "Lấy thông tin tài khoản thành công");
	}

	@GetMapping("/{accountId}")
	public ResponseEntity<?> getAccount(@PathVariable String accountId) {
		Account account = adminAccountService.findUserAccountById(accountId);
		return responseFactory.success(account, "Lấy thông tin tài khoản thành công");
	}

	@GetMapping
	public ResponseEntity<?> listAccounts(@RequestParam(required = false) String search,
			@RequestParam(defaultValue = "asc") String orderBy, @RequestParam(defaultValue = "username") String sortBy,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		SortDirection sortDirection = "asc".equalsIgnoreCase(orderBy) ? SortDirection.ASC : SortDirection.DESC;
		Page<UserAccount> accounts = adminAccountService.findUserAccounts(search, sortDirection, sortBy, page, size);
		Pagination pagination = new Pagination(accounts.getNumber(), accounts.getSize(), accounts.getTotalElements(),
				accounts.getTotalPages());
		return responseFactory.success(accounts.getContent(), "Lấy danh sách tài khoản thành công",
				pagination);
	}

}
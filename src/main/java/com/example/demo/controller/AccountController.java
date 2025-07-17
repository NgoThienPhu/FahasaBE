package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.ApiResponseDTO;
import com.example.demo.dto.PagedResponseDTO;
import com.example.demo.entities.UserAccount;
import com.example.demo.entities.bases.Account;
import com.example.demo.services.interfaces.UserAccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private UserAccountService accountService;

	public AccountController(UserAccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getUserAccounts(@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "username") String sortBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<UserAccount> accounts = accountService.getUserAccounts(orderBy, sortBy, page, size);
		PagedResponseDTO<UserAccount> pagedResponseDTO = new PagedResponseDTO<UserAccount>(accounts.getContent(),
				accounts.getNumber(), accounts.getSize(), accounts.getTotalElements(), accounts.getTotalPages(),
				accounts.isLast());
		ApiResponseDTO<PagedResponseDTO<UserAccount>> response = new ApiResponseDTO<PagedResponseDTO<UserAccount>>(
				"Lấy danh sách tài khoản thành công", "success", pagedResponseDTO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUserAccountById(@PathVariable("id") String userAccountId) {
		UserAccount account = accountService.findUserAccountById(userAccountId);
		if (account == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Không tìm thấy tài khoản với id = %s", userAccountId));
		ApiResponseDTO<Account> response = new ApiResponseDTO<Account>("Lấy thông tin tài khoản thành công", "success",
				account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}

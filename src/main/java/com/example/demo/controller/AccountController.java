package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entities.UserAccount;
import com.example.demo.entities.bases.Account;
import com.example.demo.services.interfaces.UserAccountServiceInf;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
	
	private UserAccountServiceInf accountService;

	public AccountController(UserAccountServiceInf accountService) {
		this.accountService = accountService;
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getUserAccounts() {
		List<UserAccount> accounts = accountService.getUserAccounts();
		ApiResponse<List<UserAccount>> response = new ApiResponse<List<UserAccount>>("Lấy danh sách tài khoản thành công", accounts);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserAccountById(@PathVariable("id") String userAccountId) {
		UserAccount account = accountService.findUserAccountById(userAccountId);
		if(account == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Không tìm thấy tài khoản với id = %s", userAccountId));
		ApiResponse<Account> response = new ApiResponse<Account>("Lấy thông tin tài khoản thành công", account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}

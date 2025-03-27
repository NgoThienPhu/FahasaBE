package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entities.UserAccount;
import com.example.demo.entities.bases.Account;
import com.example.demo.services.interfaces.AccountServiceInf;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
	
	private AccountServiceInf accountService;

	public AccountController(AccountServiceInf accountService) {
		this.accountService = accountService;
	}
	
	@GetMapping
	public ResponseEntity<?> getAccounts() {
		List<Account> accounts = accountService.getAccounts();
		ApiResponse<List<Account>> response = new ApiResponse<List<Account>>("Lấy danh sách tài khoản thành công", accounts);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getAccountById(@PathVariable("id") String userAccountId) {
		UserAccount account = accountService.findUserAccountById(userAccountId);
		if(account == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản");
		ApiResponse<Account> response = new ApiResponse<Account>("Lấy thông tin tài khoản thành công", account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping
//	Thiếu ràng buộc thông tin truyền về server
	public ResponseEntity<?> createAccount(@RequestBody UserAccount userAccount) {
		UserAccount account = accountService.createUserAccount(userAccount);
		ApiResponse<Account> response = new ApiResponse<Account>("Tạo tài khoản thành công", account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}

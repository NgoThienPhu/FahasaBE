package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.entities.UserAccount;
import com.example.demo.entities.bases.Account;
import com.example.demo.entities.enums.DeviceType;
import com.example.demo.services.implement.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
	
	private AuthenticationService authenticationService;
	private PasswordEncoder passwordEncoder;

	public AuthenticationController(AuthenticationService authenticationService, PasswordEncoder passwordEncoder) {
		this.authenticationService = authenticationService;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping("/login")
//	Thiếu ràng buộc thông tin truyền về server
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, @RequestHeader("User-Agent") String userAgent) {
		DeviceType deviceType = null;
		userAgent = userAgent.toLowerCase();
		
		if (userAgent.contains("windows") || userAgent.contains("macintosh") || userAgent.contains("mac os")) {
	        deviceType = DeviceType.WINDOW;
	    } else if (userAgent.contains("android") || userAgent.contains("iphone") || userAgent.contains("ipad") || userAgent.contains("ios")) {
	        deviceType = DeviceType.MOBILE;
	    } else {
	        deviceType = DeviceType.OTHERS;
	    }
		
		UserAccount account = authenticationService.login(loginRequest, deviceType);
		ApiResponse<UserAccount> response  = new ApiResponse<UserAccount>("Đăng nhập thành công!", account);
		return new ResponseEntity<ApiResponse<UserAccount>>(response, HttpStatus.OK);
	}
	
	@PostMapping("/register")
//	Thiếu ràng buộc thông tin truyền về server
	public ResponseEntity<?> register(@RequestBody UserAccount userAccount) {
		userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
		UserAccount account = authenticationService.register(userAccount);
		ApiResponse<Account> response = new ApiResponse<Account>("Tạo tài khoản thành công", account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}

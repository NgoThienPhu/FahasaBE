package com.example.demo.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auth.dto.LoginRequestDTO;
import com.example.demo.auth.dto.LoginResponseDTO;
import com.example.demo.auth.dto.RefreshAccessTokenResponseDTO;
import com.example.demo.auth.service.AdminAuthenticationService;
import com.example.demo.auth.service.AuthenticationService;
import com.example.demo.util.entity.CustomUserDetails;
import com.example.demo.util.enums.AccountType;
import com.example.demo.util.response.ResponseFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthenticationController {

	private AuthenticationService authenticationService;
	private AdminAuthenticationService adminAuthenticationService;
	private ResponseFactory responseFactory;

	public AdminAuthenticationController(AuthenticationService authenticationService,
			AdminAuthenticationService adminAuthenticationService, ResponseFactory responseFactory) {
		this.authenticationService = authenticationService;
		this.adminAuthenticationService = adminAuthenticationService;
		this.responseFactory = responseFactory;
	}

	@PostMapping("/login")
	public ResponseEntity<?> adminLogin(@Valid @RequestBody LoginRequestDTO body, HttpServletResponse response) {

		LoginResponseDTO account = adminAuthenticationService.login(body, response);
		return responseFactory.success(account, "Đăng nhập thành công!");
	}

	@PostMapping("/logout")
	public ResponseEntity<?> adminLogout(@AuthenticationPrincipal CustomUserDetails currentUser,
			HttpServletResponse response) {
		authenticationService.logout(currentUser.getUsername(), response);
		return responseFactory.success("Đăng xuất thành công!");
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
		RefreshAccessTokenResponseDTO newAccessToken = authenticationService.refreshAccessToken(request,
				AccountType.ADMIN);
		return responseFactory.success(newAccessToken, "Làm mới access token thành công");
	}

}
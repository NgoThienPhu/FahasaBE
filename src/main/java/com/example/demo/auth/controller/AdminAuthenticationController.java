package com.example.demo.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auth.dto.LoginRequestDTO;
import com.example.demo.auth.dto.LoginResponseDTO;
import com.example.demo.auth.dto.RefreshAccessTokenResponseDTO;
import com.example.demo.auth.service.AdminAuthenticationService;
import com.example.demo.auth.service.AuthenticationService;
import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponseSuccessDTO;
import com.example.demo.util.entity.CustomUserDetails;
import com.example.demo.util.validation.BindingResultUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthenticationController {

	private AuthenticationService authenticationService;
	private AdminAuthenticationService adminAuthenticationService;

	public AdminAuthenticationController(AuthenticationService authenticationService,
			AdminAuthenticationService adminAuthenticationService) {
		this.authenticationService = authenticationService;
		this.adminAuthenticationService = adminAuthenticationService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> adminLogin(@Valid @RequestBody LoginRequestDTO body, HttpServletRequest request,
			HttpServletResponse response, BindingResult result) {

		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Đăng nhập thất bại!",
				request.getRequestURI());

		if (responseError != null)
			return responseError;

		LoginResponseDTO account = adminAuthenticationService.login(body, response);
		var myResponse = new ApiResponseSuccessDTO<LoginResponseDTO>(200, "Đăng nhập thành công!", account);
		return new ResponseEntity<ApiResponseDTO>(myResponse, HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> adminLogout(@AuthenticationPrincipal CustomUserDetails currentUser,
			HttpServletResponse response) {
		authenticationService.logout(currentUser.getId(), response);
		var myResponse = new ApiResponseSuccessDTO<Void>(200, "Đăng xuất thành công!");
		return new ResponseEntity<ApiResponseDTO>(myResponse, HttpStatus.OK);
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
		RefreshAccessTokenResponseDTO newAccessToken = authenticationService.refreshAccessToken(request);
		var myResponse = new ApiResponseSuccessDTO<RefreshAccessTokenResponseDTO>(200,
				"Làm mới access token thành công", newAccessToken);
		return new ResponseEntity<ApiResponseDTO>(myResponse, HttpStatus.OK);
	}

}

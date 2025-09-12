package com.example.demo.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.account.dto.CreateUserRequestDTO;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.entity.base.Account;
import com.example.demo.auth.dto.ChangePasswordRequestDTO;
import com.example.demo.auth.dto.LoginRequestDTO;
import com.example.demo.auth.dto.LoginResponseDTO;
import com.example.demo.auth.dto.RefreshAccessTokenResponseDTO;
import com.example.demo.auth.service.AuthenticationService;
import com.example.demo.auth.service.impl.AuthenticationServiceImpl;
import com.example.demo.common.base.dto.ApiResponseDTO;
import com.example.demo.common.validation.BindingResultUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	private AuthenticationService authenticationService;

	public AuthenticationController(AuthenticationServiceImpl authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	@PostMapping("/send-otp")
	public ResponseEntity<?> sendOtp(@RequestParam String toEmail) {
		authenticationService.sendOtp(toEmail);
		var myResponse = new ApiResponseDTO<Void>("Gửi mã otp thành công!", true);
		return new ResponseEntity<ApiResponseDTO<Void>>(myResponse, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequestDTO body, BindingResult result, HttpServletResponse response) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Đăng nhập thất bại!");
		if (responseError != null)
			return responseError;

		LoginResponseDTO account = authenticationService.login(body, response);
		var myResponse = new ApiResponseDTO<LoginResponseDTO>("Đăng nhập thành công!", true, account);
		return new ResponseEntity<ApiResponseDTO<LoginResponseDTO>>(myResponse, HttpStatus.OK);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> userLogout(HttpServletResponse response) {
		authenticationService.logout(response);
		var myResponse = new ApiResponseDTO<Void>("Đăng xuất thành công!", true);
		return new ResponseEntity<ApiResponseDTO<Void>>(myResponse, HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<?> userRegister(@Valid @RequestBody CreateUserRequestDTO body, BindingResult result) {

		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Đăng kí thất bại!");
		if (responseError != null)
			return responseError;

		UserAccount account = authenticationService.userRegister(body);
		var response = new ApiResponseDTO<Account>("Đăng kí thành công", true, account);
		return new ResponseEntity<ApiResponseDTO<Account>>(response, HttpStatus.OK);
	}

	@PatchMapping("/change-password")
	public ResponseEntity<?> userChangePassword(@Valid @RequestBody ChangePasswordRequestDTO body,
			@AuthenticationPrincipal UserDetails currentUser, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Đổi mật khẩu thất bại!");
		if (responseError != null)
			return responseError;
		authenticationService.changePassword(body, currentUser);
		var response = new ApiResponseDTO<Void>("Đổi mật khẩu thành công", true);
		return new ResponseEntity<ApiResponseDTO<Void>>(response, HttpStatus.OK);
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
		RefreshAccessTokenResponseDTO newAccessToken = authenticationService.refreshTokenAccess(request, response);
		var myResponse = new ApiResponseDTO<RefreshAccessTokenResponseDTO>("Làm mới Access token thành công", true, newAccessToken);
		return new ResponseEntity<ApiResponseDTO<RefreshAccessTokenResponseDTO>>(myResponse, HttpStatus.OK);
	}

}

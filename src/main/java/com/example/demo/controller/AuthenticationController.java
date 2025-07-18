package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponseDTO;
import com.example.demo.dto.ChangePasswordRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.UserAccountRegisterRequestDTO;
import com.example.demo.entities.UserAccount;
import com.example.demo.entities.bases.Account;
import com.example.demo.services.implement.AuthenticationServiceImpl;
import com.example.demo.utils.BindingResultUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	private AuthenticationServiceImpl authenticationServiceImpl;

	public AuthenticationController(AuthenticationServiceImpl authenticationServiceImpl) {
		this.authenticationServiceImpl = authenticationServiceImpl;
	}

	@GetMapping("/login")
	public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequestDTO body, BindingResult result) {

		ResponseEntity<?> responseError = BindingResultUtils.handleValidationErrors(result, "Đăng nhập thất bại!");
		if (responseError != null)
			return responseError;

		LoginResponseDTO account = authenticationServiceImpl.userLogin(body);
		ApiResponseDTO<LoginResponseDTO> response = new ApiResponseDTO<LoginResponseDTO>("Đăng nhập thành công!",
				"success", account);
		return new ResponseEntity<ApiResponseDTO<LoginResponseDTO>>(response, HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<?> userRegister(@Valid @RequestBody UserAccountRegisterRequestDTO body, BindingResult result) {

		ResponseEntity<?> responseError = BindingResultUtils.handleValidationErrors(result, "Đăng kí thất bại!");
		if (responseError != null)
			return responseError;

		UserAccount account = authenticationServiceImpl.userRegister(body);
		ApiResponseDTO<Account> response = new ApiResponseDTO<Account>("Đăng kí thành công", "success", account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/change-password")
	public ResponseEntity<?> userChangePassword(@Valid @RequestBody ChangePasswordRequestDTO body, BindingResult result,
			@RequestHeader("Authorization") String authHeader) {
		ResponseEntity<?> responseError = BindingResultUtils.handleValidationErrors(result, "Đổi mật khẩu thất bại!");
		if (responseError != null)
			return responseError;
		String accessToken = authHeader.replace("Bearer ", "");
		authenticationServiceImpl.userChangePassword(body, accessToken);
		ApiResponseDTO<Void> response = new ApiResponseDTO<Void>("Đổi mật khẩu thành công", "success");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}

package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponseDTO;
import com.example.demo.dto.ChangePasswordRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.CreateUserRequestDTO;
import com.example.demo.entities.UserAccount;
import com.example.demo.entities.bases.Account;
import com.example.demo.services.implement.AuthenticationServiceImpl;
import com.example.demo.services.interfaces.AuthenticationService;
import com.example.demo.util.validation.BindingResultUtil;
import com.example.demo.util.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	private AuthenticationService authenticationService;

	public AuthenticationController(AuthenticationServiceImpl authenticationService) {
		this.authenticationService = authenticationService;
	}

	@GetMapping("/login")
	@JsonView(View.Self.class)
	public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequestDTO body, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Đăng nhập thất bại!");
		if (responseError != null)
			return responseError;

		LoginResponseDTO account = authenticationService.userLogin(body);
		ApiResponseDTO<LoginResponseDTO> response = new ApiResponseDTO<LoginResponseDTO>("Đăng nhập thành công!",
				"success", account);
		return new ResponseEntity<ApiResponseDTO<LoginResponseDTO>>(response, HttpStatus.OK);
	}

	@PostMapping("/register")
	@JsonView(View.Self.class)
	public ResponseEntity<?> userRegister(@Valid @RequestBody CreateUserRequestDTO body,
			BindingResult result) {

		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Đăng kí thất bại!");
		if (responseError != null)
			return responseError;

		UserAccount account = authenticationService.userRegister(body);
		ApiResponseDTO<Account> response = new ApiResponseDTO<Account>("Đăng kí thành công", "success", account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/change-password")
	@JsonView(View.Self.class)
	public ResponseEntity<?> userChangePassword(@Valid @RequestBody ChangePasswordRequestDTO body, @AuthenticationPrincipal UserDetails currentUser, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Đổi mật khẩu thất bại!");
		if (responseError != null)
			return responseError;
		authenticationService.userChangePassword(body, currentUser);
		ApiResponseDTO<Void> response = new ApiResponseDTO<Void>("Đổi mật khẩu thành công", "success");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}

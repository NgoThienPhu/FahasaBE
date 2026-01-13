package com.example.demo.auth.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponseErrorDTO;
import com.example.demo.util.dto.api_response.ApiResponseSuccessDTO;
import com.example.demo.util.entity.CustomUserDetails;
import com.example.demo.util.validation.BindingResultUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	private AuthenticationService authenticationService;

	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@PostMapping("/send-otp")
	public ResponseEntity<?> sendOtp(@RequestParam String toEmail) {
		authenticationService.sendOtp(toEmail);
		var myResponse = new ApiResponseSuccessDTO<Void>(200, "Gửi mã otp thành công!");
		return new ResponseEntity<ApiResponseDTO>(myResponse, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequestDTO body, HttpServletRequest request,
			HttpServletResponse response, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Đăng nhập thất bại!",
				request.getRequestURI());
		if (responseError != null)
			return responseError;

		LoginResponseDTO account = authenticationService.login(body, response);
		var myResponse = new ApiResponseSuccessDTO<LoginResponseDTO>(200, "Đăng nhập thành công!", account);
		return new ResponseEntity<ApiResponseDTO>(myResponse, HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> userLogout(@AuthenticationPrincipal CustomUserDetails currentUser,
			HttpServletResponse response) {
		authenticationService.logout(currentUser.getId(), response);
		var myResponse = new ApiResponseSuccessDTO<Void>(200, "Đăng xuất thành công!");
		return new ResponseEntity<ApiResponseDTO>(myResponse, HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<?> userRegister(@Valid @RequestBody CreateUserRequestDTO body, BindingResult result,
			HttpServletRequest request) {

		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Đăng kí thất bại!",
				request.getRequestURI());
		
		Map<String, String> errors = authenticationService.validateUniqueUserFields(body.username(), body.email(), body.phoneNumber());
		
		if(!errors.isEmpty()) {
			var response = new ApiResponseErrorDTO(400, "Đăng kí thất bại", "VALIDATION_ERROR", errors, request.getRequestURI());
	        responseError = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (responseError != null)
			return responseError;

		UserAccount account = authenticationService.userRegister(body);
		var response = new ApiResponseSuccessDTO<Account>(201, "Đăng kí thành công", account);
		
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@PatchMapping("/change-password")
	public ResponseEntity<?> userChangePassword(@Valid @RequestBody ChangePasswordRequestDTO body,
			@AuthenticationPrincipal CustomUserDetails currentUser, HttpServletRequest request, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Đổi mật khẩu thất bại!",
				request.getRequestURI());
		if (responseError != null)
			return responseError;
		authenticationService.changePassword(body, currentUser);
		var response = new ApiResponseSuccessDTO<Void>(200, "Đổi mật khẩu thành công");
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
		RefreshAccessTokenResponseDTO newAccessToken = authenticationService.refreshTokenAccess(request);
		var myResponse = new ApiResponseSuccessDTO<RefreshAccessTokenResponseDTO>(200,
				"Làm mới access token thành công", newAccessToken);
		return new ResponseEntity<ApiResponseDTO>(myResponse, HttpStatus.OK);
	}

}

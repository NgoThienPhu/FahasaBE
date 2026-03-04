package com.example.demo.auth.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.example.demo.auth.dto.RessetPasswordRequestDTO;
import com.example.demo.auth.dto.VerifyRessetPasswordTokenRequest;
import com.example.demo.auth.dto.VerifyRessetPasswordTokenResponse;
import com.example.demo.auth.service.AuthenticationService;
import com.example.demo.auth.service.UserAuthenticationService;
import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponseErrorDTO;
import com.example.demo.util.dto.api_response.ApiResponseSuccessDTO;
import com.example.demo.util.entity.CustomUserDetails;
import com.example.demo.util.service.JwtService;
import com.example.demo.util.service.MessageService;
import com.example.demo.util.validation.BindingResultUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UserAuthenticationController {

	private UserAuthenticationService userAuthenticationService;
	private AuthenticationService authenticationService;
	private JwtService jwtService;
	private MessageService messageService;

	public UserAuthenticationController(UserAuthenticationService userAuthenticationService,
			AuthenticationService authenticationService, JwtService jwtService, MessageService messageService) {
		this.userAuthenticationService = userAuthenticationService;
		this.authenticationService = authenticationService;
		this.jwtService = jwtService;
		this.messageService = messageService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequestDTO body, HttpServletRequest request,
			HttpServletResponse response, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Đăng nhập thất bại!",
				request.getRequestURI());
		if (responseError != null)
			return responseError;

		LoginResponseDTO account = userAuthenticationService.login(body, response);
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

		Map<String, String> errors = userAuthenticationService.validateUniqueUserFields(body.username(), body.email(),
				body.phoneNumber());

		if (!errors.isEmpty()) {
			var response = new ApiResponseErrorDTO(400, "Đăng kí thất bại", "VALIDATION_ERROR", errors,
					request.getRequestURI());
			responseError = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (responseError != null)
			return responseError;

		UserAccount account = userAuthenticationService.register(body);
		var response = new ApiResponseSuccessDTO<Account>(201, "Đăng kí thành công", account);

		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@PutMapping("/change-password")
	public ResponseEntity<?> userChangePassword(@Valid @RequestBody ChangePasswordRequestDTO body,
			@AuthenticationPrincipal CustomUserDetails currentUser, HttpServletRequest request, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Đổi mật khẩu thất bại!",
				request.getRequestURI());
		if (responseError != null)
			return responseError;
		userAuthenticationService.changePassword(body, currentUser);
		var response = new ApiResponseSuccessDTO<Void>(200, "Đổi mật khẩu thành công");
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
		RefreshAccessTokenResponseDTO newAccessToken = authenticationService.refreshAccessToken(request,
				Account.AccountType.USER);
		var myResponse = new ApiResponseSuccessDTO<RefreshAccessTokenResponseDTO>(200,
				"Làm mới access token thành công", newAccessToken);
		return new ResponseEntity<ApiResponseDTO>(myResponse, HttpStatus.OK);
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestParam(required = true) String email) {
		String ressetPasswordToken = jwtService.createToken(email, Account.TokenType.RESSET_PASSWORD);
		messageService.sendRessetPasswordEmail(email, "Yêu cầu đặt lại mật khẩu", ressetPasswordToken);
		var myResponse = new ApiResponseSuccessDTO<Void>(200,
				"Yêu cầu đặt lại mật khẩu đã được gửi đến email của bạn!");
		return new ResponseEntity<ApiResponseDTO>(myResponse, HttpStatus.OK);
	}

	@PostMapping("/resset-password")
	public ResponseEntity<?> ressetPassword(@Valid @RequestBody RessetPasswordRequestDTO dto,
			HttpServletRequest request, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Đặt lại mật khẩu thất bại!",
				request.getRequestURI());
		if (responseError != null)
			return responseError;
		authenticationService.ressetPassword(dto.ressetPasswordToken(), dto.newPassword());
		var myResponse = new ApiResponseSuccessDTO<Void>(200, "Đặt lại mật khẩu thành công!");
		return new ResponseEntity<ApiResponseDTO>(myResponse, HttpStatus.OK);
	}

	@PostMapping("/resset-password/verify")
	public ResponseEntity<?> verifyRessetPasswordToken(@Valid @RequestBody VerifyRessetPasswordTokenRequest dto,
			HttpServletRequest request, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Xác thực RessetPasswordToken thất bại!",
				request.getRequestURI());
		if (responseError != null)
			return responseError;
		boolean valid = authenticationService.verifyRessetPasswordToken(dto.ressetPasswordToken());
		var myResponse = new ApiResponseSuccessDTO<VerifyRessetPasswordTokenResponse>(200, "Xác thực RessetPasswordToken thành công!",
				new VerifyRessetPasswordTokenResponse(valid));
		return new ResponseEntity<ApiResponseDTO>(myResponse, HttpStatus.OK);

	}

}

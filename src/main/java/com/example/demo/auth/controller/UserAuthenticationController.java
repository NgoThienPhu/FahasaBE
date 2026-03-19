package com.example.demo.auth.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.account.dto.CreateUserRequestDTO;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.auth.dto.ChangePasswordRequestDTO;
import com.example.demo.auth.dto.LoginRequestDTO;
import com.example.demo.auth.dto.LoginResponseDTO;
import com.example.demo.auth.dto.RefreshAccessTokenResponseDTO;
import com.example.demo.auth.dto.RessetPasswordRequestDTO;
import com.example.demo.auth.dto.VerifyRessetPasswordTokenRequest;
import com.example.demo.auth.dto.VerifyRessetPasswordTokenResponse;
import com.example.demo.auth.service.AuthenticationService;
import com.example.demo.auth.service.UserAuthenticationService;
import com.example.demo.util.entity.CustomUserDetails;
import com.example.demo.util.enums.AccountType;
import com.example.demo.util.enums.TokenType;
import com.example.demo.util.service.JwtService;
import com.example.demo.util.service.MessageService;
import com.example.demo.util.response.ResponseFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UserAuthenticationController {

	private AuthenticationService authenticationService;
	private UserAuthenticationService userAuthenticationService;
	private AccountRepository accountRepository;
	private ResponseFactory responseFactory;
	private JwtService jwtService;
	private MessageService messageService;

	public UserAuthenticationController(AuthenticationService authenticationService,
			UserAuthenticationService userAuthenticationService, AccountRepository accountRepository,
			ResponseFactory responseFactory, JwtService jwtService, MessageService messageService) {
		this.authenticationService = authenticationService;
		this.userAuthenticationService = userAuthenticationService;
		this.accountRepository = accountRepository;
		this.responseFactory = responseFactory;
		this.jwtService = jwtService;
		this.messageService = messageService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequestDTO body, HttpServletResponse response) {

		LoginResponseDTO account = userAuthenticationService.login(body, response);
		return responseFactory.success(account, "Đăng nhập thành công");
	}

	@PostMapping("/logout")
	public ResponseEntity<?> userLogout(@AuthenticationPrincipal CustomUserDetails currentUser,
			HttpServletResponse response) {
		authenticationService.logout(currentUser.getUsername(), response);
		return responseFactory.success("Đăng xuất thành công");
	}

	@PostMapping("/register")
	public ResponseEntity<?> userRegister(@Valid @RequestBody CreateUserRequestDTO dto) {

		UserAccount account = userAuthenticationService.register(dto);

		return responseFactory.success(account, "Đăng kí thành công");
	}

	@PutMapping("/change-password")
	public ResponseEntity<?> userChangePassword(@Valid @RequestBody ChangePasswordRequestDTO body,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		userAuthenticationService.changePassword(body, currentUser);
		return responseFactory.success("Đổi mật khẩu thành công");
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
		RefreshAccessTokenResponseDTO newAccessToken = authenticationService.refreshAccessToken(request,
				AccountType.USER);
		return responseFactory.success(newAccessToken, "Làm mới access token thành công");
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestParam(required = true) String email) {
		String ressetPasswordToken = jwtService.createToken(email, TokenType.RESSET_PASSWORD);
		messageService.sendRessetPasswordEmail(email, "Yêu cầu đặt lại mật khẩu", ressetPasswordToken);
		return responseFactory.success("Yêu cầu đặt lại mật khẩu đã được gửi đến email của bạn!");
	}

	@PostMapping("/resset-password")
	public ResponseEntity<?> ressetPassword(@Valid @RequestBody RessetPasswordRequestDTO dto) {
		authenticationService.ressetPassword(dto.ressetPasswordToken(), dto.newPassword());
		return responseFactory.success("Đặt lại mật khẩu thành công!");
	}

	@PostMapping("/resset-password/verify")
	public ResponseEntity<?> verifyRessetPasswordToken(@Valid @RequestBody VerifyRessetPasswordTokenRequest dto) {
		boolean valid = authenticationService.verifyRessetPasswordToken(dto.ressetPasswordToken());
		return responseFactory.success(new VerifyRessetPasswordTokenResponse(valid),
				"Xác thực RessetPasswordToken thành công!");

	}

}
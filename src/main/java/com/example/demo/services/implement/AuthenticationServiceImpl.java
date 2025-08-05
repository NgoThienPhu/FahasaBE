package com.example.demo.services.implement;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.ChangePasswordRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.CreateUserRequestDTO;
import com.example.demo.entities.Email;
import com.example.demo.entities.PhoneNumber;
import com.example.demo.entities.UserAccount;
import com.example.demo.entities.enums.TokenType;
import com.example.demo.services.interfaces.AuthenticationService;
import com.example.demo.services.interfaces.JwtService;
import com.example.demo.services.interfaces.UserAccountService;

import jakarta.transaction.Transactional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private AuthenticationManager authenticationManager;
	private UserAccountService userAccountService;
	private JwtService jwtService;
	private PasswordEncoder passwordEncoder;

	public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserAccountService userAccountService,
			JwtService jwtService, PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.userAccountService = userAccountService;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public LoginResponseDTO userLogin(LoginRequestDTO body) {
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(body.username(), body.password()));

			if (!authentication.isAuthenticated()) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
						"Tài khoản hoặc mật khẩu không chính xác, vui lòng đăng nhập lại");
			}

			UserAccount account = userAccountService.findUserAccountByUsername(body.username());

			if (account.getIsActive() == false)
				throw new ResponseStatusException(HttpStatus.LOCKED,
						"Tài khoản của bạn đã bị khóa vui lòng thử lại sau");

			String accessToken = jwtService.createToken(account.getUsername(), TokenType.ACCESS);

			return convertUserAccountToLoginResponseDTO(account, accessToken);

		} catch (BadCredentialsException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"Tài khoản hoặc mật khẩu không chính xác, vui lòng đăng nhập lại");
		} catch (InternalAuthenticationServiceException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"Tài khoản hoặc mật khẩu không chính xác, vui lòng đăng nhập lại");
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Transactional
	@Override
	public UserAccount userRegister(CreateUserRequestDTO body) {
		Boolean checkExistsUsername = userAccountService.existsByUsername(body.username());
		Boolean checkExistsEmail = userAccountService.existsByEmail(body.email());
		Boolean checkExistsPhoneNumber = userAccountService.existsByPhoneNumber(body.phoneNumber());

		if (checkExistsUsername)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Tên đăng nhập đã tồn tại, vui lòng thử tên đăng nhập khác");
		if (checkExistsEmail)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại, vui lòng thử Email khác");
		if (checkExistsPhoneNumber)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Số điện thoại đã tồn tại, vui lòng thử số điện thoại khác");

		UserAccount account = userAccountService.createUserAccount(convertUserAccountValidatorToUserAccount(body));
		return account;
	}

	@Override
	public void userLogout() {
		try {
			SecurityContextHolder.clearContext();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Đăng xuất thất bại vui lòng thử lại sau!");
		}
	}

	@Override
	public UserAccount userForgotPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void userChangePassword(ChangePasswordRequestDTO body, UserDetails currentUser) {
		try {
			UserAccount user = userAccountService.findUserAccountByUsername(currentUser.getUsername());

			if (user == null || !passwordEncoder.matches(body.oldPassword(), user.getPassword()))
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
						"Tài khoản không tồn tại hoặc mật khẩu cũ không chính xác, vui lòng thử lại sau");

			String newPassword = passwordEncoder.encode(body.newPassword());

			user.setPassword(newPassword);

			userAccountService.save(user);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

	@Override
	public void tokenRefresh() {
		// TODO Auto-generated method stub

	}

	private UserAccount convertUserAccountValidatorToUserAccount(CreateUserRequestDTO dto) {
		UserAccount userAccount = new UserAccount();
		userAccount.setUsername(dto.username());
		userAccount.setPassword(passwordEncoder.encode(dto.password()));
		userAccount.setFullName(dto.fullName());
		userAccount.setEmail(new Email(dto.email()));
		userAccount.setPhoneNumber(new PhoneNumber(dto.phoneNumber()));
		return userAccount;
	}

	private LoginResponseDTO convertUserAccountToLoginResponseDTO(UserAccount account, String accessToken) {
		LoginResponseDTO loginResponseDTO = new LoginResponseDTO(account.getAccountId(), account.getUsername(),
				account.getFullName(), account.getEmail(), account.getPhoneNumber(), accessToken);
		return loginResponseDTO;
	}

}

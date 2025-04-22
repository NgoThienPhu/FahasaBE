package com.example.demo.services.implement;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.entities.UserAccount;
import com.example.demo.entities.enums.DeviceType;
import com.example.demo.entities.enums.TokenType;
import com.example.demo.repository.AccountRepository;
import com.example.demo.services.interfaces.AuthenticationServiceInf;
import com.example.demo.services.interfaces.JwtServiceInf;
import com.example.demo.services.interfaces.UserAccountServiceInf;
import com.example.demo.validator.LoginValidator;
import com.example.demo.validator.UserAccountValidator;

import jakarta.transaction.Transactional;

@Service
public class AuthenticationService implements AuthenticationServiceInf {

	private AuthenticationManager authenticationManager;
	private UserAccountServiceInf userAccountService;
	private AccountRepository accountRepository;
	private JwtServiceInf jwtService;

	public AuthenticationService(AuthenticationManager authenticationManager, UserAccountServiceInf userAccountService,
			AccountRepository accountRepository, JwtServiceInf jwtService) {
		this.authenticationManager = authenticationManager;
		this.userAccountService = userAccountService;
		this.accountRepository = accountRepository;
		this.jwtService = jwtService;
	}

	@Override
	public LoginResponseDTO login(LoginValidator body, DeviceType deviceType) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword()));
		if (authentication.isAuthenticated()) {
			UserAccount account = userAccountService.findUserAccountByUsername(body.getUsername());
			String accessToken = jwtService.createToken(account.getUsername(), TokenType.ACCESS, deviceType);
			LoginResponseDTO loginResponse = convertUserAccountToLoginResponseDTO(account, accessToken);
			return loginResponse;
		}
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
				"Tài khoản hoặc mật khẩu không chính xác, vui lòng đăng nhập lại");
	}

	@Transactional
	@Override
	public UserAccount register(UserAccountValidator body) {
		Boolean checkExistsUsername = accountRepository.existsByUsername(body.getUsername());
		Boolean checkExistsEmail = userAccountService.existsByEmail(body.getEmail());
		Boolean checkExistsPhoneNumber = userAccountService.existsByPhoneNumber(body.getPhoneNumber());
		
		if(checkExistsUsername) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên đăng nhập đã tồn tại, vui lòng thử tên đăng nhập khác");
		if(checkExistsEmail) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại, vui lòng thử Email khác");
		if(checkExistsPhoneNumber) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số điện thoại đã tồn tại, vui lòng thử số điện thoại khác");
		
		UserAccount account = userAccountService.createUserAccount(convertUserAccountValidatorToUserAccount(body));
		return account;
	}

	@Override
	public Boolean logout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserAccount forgotPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserAccount changePassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void tokenRefresh() {
		// TODO Auto-generated method stub

	}
	
	private UserAccount convertUserAccountValidatorToUserAccount(UserAccountValidator userAccountValidator) {
		UserAccount userAccount = new UserAccount();
		userAccount.setUsername(userAccountValidator.getUsername());
		userAccount.setPassword(userAccountValidator.getPassword());
		userAccount.setFullName(userAccountValidator.getFullName());
		userAccount.setEmail(userAccountValidator.getEmail());
		userAccount.setPhoneNumber(userAccountValidator.getPhoneNumber());
		return userAccount;
	}

	private LoginResponseDTO convertUserAccountToLoginResponseDTO(UserAccount account, String accessToken) {
		LoginResponseDTO loginResponseDTO = new LoginResponseDTO(account.getId(), account.getUsername(), null,
				account.getRole(), account.getCreatedAt(), account.getUpdatedAt(), account.getFullName(),
				account.getEmail(), account.getPhoneNumber(), account.getIsActive(), accessToken);
		return loginResponseDTO;
	}

}

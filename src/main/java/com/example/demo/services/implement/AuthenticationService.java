package com.example.demo.services.implement;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.entities.UserAccount;
import com.example.demo.entities.enums.DeviceType;
import com.example.demo.entities.enums.TokenType;
import com.example.demo.repository.AccountRepository;
import com.example.demo.services.interfaces.AuthenticationServiceInf;
import com.example.demo.services.interfaces.JwtServiceInf;
import com.example.demo.services.interfaces.UserAccountServiceInf;

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
	public LoginResponseDTO login(LoginRequestDTO loginRequest, DeviceType deviceType) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
		if (authentication.isAuthenticated()) {
			UserAccount account = userAccountService.findUserAccountByUsername(loginRequest.username());
			String accessToken = jwtService.createToken(account.getUsername(), TokenType.ACCESS, deviceType);
			LoginResponseDTO loginResponse = convertUserAccountToLoginResponseDTO(account, accessToken);
			return loginResponse;
		}
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
				"Tài khoản hoặc mật khẩu không chính xác, vui lòng đăng nhập lại");
	}

	@Override
	public UserAccount register(UserAccount userAccount) {
		Boolean checkExistsUsername = accountRepository.existsByUsername(userAccount.getUsername());
		Boolean checkExistsEmail = userAccountService.existsByEmail(userAccount.getEmail());
		Boolean checkExistsPhoneNumber = userAccountService.existsByPhoneNumber(userAccount.getPhoneNumber());
		
		if(checkExistsUsername) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên đăng nhập đã tồn tại, vui lòng thử tên đăng nhập khác");
		if(checkExistsEmail) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại, vui lòng thử Email khác");
		if(checkExistsPhoneNumber) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số điện thoại đã tồn tại, vui lòng thử số điện thoại khác");
		
		UserAccount account = userAccountService.createUserAccount(userAccount);
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

	private LoginResponseDTO convertUserAccountToLoginResponseDTO(UserAccount account, String accessToken) {
		LoginResponseDTO loginResponseDTO = new LoginResponseDTO(account.getId(), account.getUsername(), null,
				account.getRole(), account.getCreatedAt(), account.getUpdatedAt(), account.getFullName(),
				account.getEmail(), account.getPhoneNumber(), account.getIsActive(), accessToken);
		return loginResponseDTO;
	}

}

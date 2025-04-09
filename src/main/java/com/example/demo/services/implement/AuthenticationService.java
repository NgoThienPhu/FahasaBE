package com.example.demo.services.implement;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.LoginRequest;
import com.example.demo.entities.UserAccount;
import com.example.demo.entities.enums.DeviceType;
import com.example.demo.entities.enums.TokenType;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.services.interfaces.AuthenticationServiceInf;
import com.example.demo.services.interfaces.JwtServiceInf;
import com.example.demo.services.interfaces.UserAccountServiceInf;

@Service
public class AuthenticationService implements AuthenticationServiceInf {
	
	private AuthenticationManager authenticationManager;
	private UserAccountRepository userAccountRepository;
	private UserAccountServiceInf userAccountService;
	private JwtServiceInf jwtService;

	public AuthenticationService(AuthenticationManager authenticationManager,
			UserAccountRepository userAccountRepository, UserAccountServiceInf userAccountService,
			JwtServiceInf jwtService) {
		this.authenticationManager = authenticationManager;
		this.userAccountRepository = userAccountRepository;
		this.userAccountService = userAccountService;
		this.jwtService = jwtService;
	}

	@Override
	public UserAccount login(LoginRequest loginRequest, DeviceType deviceType) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
				);
		if(authentication.isAuthenticated()) {
			UserAccount account = userAccountService.findUserAccountByUsername(loginRequest.username());
			String accessToken = jwtService.createToken(account.getUsername(), TokenType.ACCESS, deviceType);
			return account;
		}
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản hoặc mật khẩu không chính xác, vui lòng đăng nhập lại");
	}

	@Override
	public UserAccount register(UserAccount userAccount) {
		UserAccount account = userAccountRepository.save(userAccount);
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

}

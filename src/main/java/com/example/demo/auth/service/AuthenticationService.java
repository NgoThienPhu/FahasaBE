package com.example.demo.auth.service;

import java.util.Random;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.account.dto.CreateUserRequestDTO;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.auth.dto.ChangePasswordRequestDTO;
import com.example.demo.auth.dto.LoginRequestDTO;
import com.example.demo.auth.dto.LoginResponseDTO;

public interface AuthenticationService {

	LoginResponseDTO login(LoginRequestDTO body);

	UserAccount userRegister(CreateUserRequestDTO body);

	void logout();

	UserAccount userForgotPassword();

	void changePassword(ChangePasswordRequestDTO body, UserDetails currentUser);

	void tokenRefresh();

	static String generate6DigitCode() {
		Random random = new Random();
		int code = 100_000 + random.nextInt(900_000);
		return String.valueOf(code);
	}
}

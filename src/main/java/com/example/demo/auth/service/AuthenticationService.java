package com.example.demo.auth.service;

import java.util.Random;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.account.dto.CreateUserRequestDTO;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.auth.dto.ChangePasswordRequestDTO;
import com.example.demo.auth.dto.LoginRequestDTO;
import com.example.demo.auth.dto.LoginResponseDTO;
import com.example.demo.auth.dto.RefreshAccessTokenResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {

	LoginResponseDTO login(LoginRequestDTO body, HttpServletResponse response);

	UserAccount userRegister(CreateUserRequestDTO body);

	void logout(HttpServletResponse response);

	UserAccount userForgotPassword();

	void changePassword(ChangePasswordRequestDTO body, UserDetails currentUser);

	RefreshAccessTokenResponseDTO refreshTokenAccess(HttpServletRequest request, HttpServletResponse response);
	
	void sendOtp(String email);

	static String generate6DigitCode() {
		Random random = new Random();
		int code = 100_000 + random.nextInt(900_000);
		return String.valueOf(code);
	}
}

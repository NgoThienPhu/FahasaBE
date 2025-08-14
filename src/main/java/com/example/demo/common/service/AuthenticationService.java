package com.example.demo.common.service;

import java.util.Random;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.account.dto.CreateUserRequestDTO;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.common.base.dto.ChangePasswordRequestDTO;
import com.example.demo.common.base.dto.LoginRequestDTO;
import com.example.demo.common.base.dto.LoginResponseDTO;

public interface AuthenticationService {
	
	public LoginResponseDTO login(LoginRequestDTO body);

	public UserAccount userRegister(CreateUserRequestDTO body);
	
	public void logout();

	public UserAccount userForgotPassword();

	public void changePassword(ChangePasswordRequestDTO body, UserDetails currentUser);

	public void tokenRefresh();
	
	public static String generate6DigitCode() {
        Random random = new Random();
        int code = 100_000 + random.nextInt(900_000);
        return String.valueOf(code);
    }
}

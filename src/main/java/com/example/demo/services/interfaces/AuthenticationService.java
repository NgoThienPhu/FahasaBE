package com.example.demo.services.interfaces;

import java.util.Random;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.dto.ChangePasswordRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.entities.account.UserAccount;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.CreateUserRequestDTO;

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

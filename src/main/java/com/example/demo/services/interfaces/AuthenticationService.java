package com.example.demo.services.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.dto.ChangePasswordRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.CreateUserRequestDTO;
import com.example.demo.entities.UserAccount;

public interface AuthenticationService {
	public LoginResponseDTO userLogin(LoginRequestDTO body);

	public UserAccount userRegister(CreateUserRequestDTO body);

	public void userLogout();

	public UserAccount userForgotPassword();

	public void userChangePassword(ChangePasswordRequestDTO body, UserDetails currentUser);

	public void tokenRefresh();
}

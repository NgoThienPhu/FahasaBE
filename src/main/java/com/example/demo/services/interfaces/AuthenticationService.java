package com.example.demo.services.interfaces;

import com.example.demo.dto.ChangePasswordRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.entities.UserAccount;
import com.example.demo.validator.LoginValidator;
import com.example.demo.validator.UserAccountValidator;

public interface AuthenticationService {
	public LoginResponseDTO userLogin(LoginValidator body);

	public UserAccount userRegister(UserAccountValidator body);

	public void userLogout();

	public UserAccount userForgotPassword();

	public void userChangePassword(ChangePasswordRequestDTO body, String accessToken);

	public void tokenRefresh();
}

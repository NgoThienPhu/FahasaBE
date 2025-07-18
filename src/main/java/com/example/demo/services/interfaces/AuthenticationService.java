package com.example.demo.services.interfaces;

import com.example.demo.dto.ChangePasswordRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.UserAccountRegisterRequestDTO;
import com.example.demo.entities.UserAccount;

public interface AuthenticationService {
	public LoginResponseDTO userLogin(LoginRequestDTO body);

	public UserAccount userRegister(UserAccountRegisterRequestDTO body);

	public void userLogout();

	public UserAccount userForgotPassword();

	public void userChangePassword(ChangePasswordRequestDTO body, String accessToken);

	public void tokenRefresh();
}

package com.example.demo.services.interfaces;

import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.entities.UserAccount;
import com.example.demo.entities.enums.DeviceType;

public interface AuthenticationServiceInf {
	public LoginResponseDTO login(LoginRequestDTO loginRequest, DeviceType deviceType);
	public UserAccount register(UserAccount userAccount);
	public Boolean logout();
	public UserAccount forgotPassword();
	public UserAccount changePassword();
	public void tokenRefresh();
}

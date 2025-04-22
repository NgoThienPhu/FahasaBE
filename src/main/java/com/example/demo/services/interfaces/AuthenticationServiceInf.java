package com.example.demo.services.interfaces;

import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.entities.UserAccount;
import com.example.demo.entities.enums.DeviceType;
import com.example.demo.validator.LoginValidator;
import com.example.demo.validator.UserAccountValidator;

public interface AuthenticationServiceInf {
	public LoginResponseDTO login(LoginValidator body, DeviceType deviceType);
	public UserAccount register(UserAccountValidator body);
	public Boolean logout();
	public UserAccount forgotPassword();
	public UserAccount changePassword();
	public void tokenRefresh();
}

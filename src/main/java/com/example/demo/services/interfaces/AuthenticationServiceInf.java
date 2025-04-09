package com.example.demo.services.interfaces;

import com.example.demo.dto.LoginRequest;
import com.example.demo.entities.UserAccount;
import com.example.demo.entities.enums.DeviceType;

public interface AuthenticationServiceInf {
	public UserAccount login(LoginRequest loginRequest, DeviceType deviceType);
	public UserAccount register(UserAccount userAccount);
	public Boolean logout();
	public UserAccount forgotPassword();
	public UserAccount changePassword();
	public void tokenRefresh();
}

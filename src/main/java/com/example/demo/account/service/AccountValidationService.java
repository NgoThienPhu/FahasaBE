package com.example.demo.account.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.account.entity.base.Account;
import com.example.demo.util.exception.CustomException;

@Service
public class AccountValidationService {
	
    private final PasswordEncoder passwordEncoder;

    public AccountValidationService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

    public void verifyPassword(Account account, String rawPassword) {

        if (!passwordEncoder.matches(rawPassword, account.getPassword())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Mật khẩu không chính xác");
        }
    }

}

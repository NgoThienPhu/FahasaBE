package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponseDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.entities.UserAccount;
import com.example.demo.entities.bases.Account;
import com.example.demo.entities.enums.DeviceType;
import com.example.demo.services.implement.AuthenticationService;
import com.example.demo.validator.LoginValidator;
import com.example.demo.validator.UserAccountValidator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
	
	private AuthenticationService authenticationService;
	private PasswordEncoder passwordEncoder;

	public AuthenticationController(AuthenticationService authenticationService, PasswordEncoder passwordEncoder) {
		this.authenticationService = authenticationService;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginValidator body, BindingResult result, @RequestHeader("User-Agent") String userAgent) {
		
		if (result.hasErrors()) {
			Map<String, String> errors = new HashMap<String, String>();
			result.getFieldErrors().stream().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});
			ApiResponseDTO<Map<String, String>> response  = new ApiResponseDTO<>("Đăng nhập thất bại!", errors);
			return new ResponseEntity<ApiResponseDTO<Map<String, String>>>(response, HttpStatus.BAD_REQUEST);
		}
		
		DeviceType deviceType = null;
		userAgent = userAgent.toLowerCase();
		
		if (userAgent.contains("windows") || userAgent.contains("macintosh") || userAgent.contains("mac os")) {
	        deviceType = DeviceType.WINDOW;
	    } else if (userAgent.contains("android") || userAgent.contains("iphone") || userAgent.contains("ipad") || userAgent.contains("ios")) {
	        deviceType = DeviceType.MOBILE;
	    } else {
	        deviceType = DeviceType.OTHERS;
	    }
		
		LoginResponseDTO account = authenticationService.login(body, deviceType);
		ApiResponseDTO<LoginResponseDTO> response  = new ApiResponseDTO<LoginResponseDTO>("Đăng nhập thành công!", account);
		return new ResponseEntity<ApiResponseDTO<LoginResponseDTO>>(response, HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody UserAccountValidator body, BindingResult result) {
		
		if (result.hasErrors()) {
			Map<String, String> errors = new HashMap<String, String>();
			result.getFieldErrors().stream().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});
			ApiResponseDTO<Map<String, String>> response  = new ApiResponseDTO<>("Đăng kí thất bại!", errors);
			return new ResponseEntity<ApiResponseDTO<Map<String, String>>>(response, HttpStatus.BAD_REQUEST);
		}
		
		body.setPassword(passwordEncoder.encode(body.getPassword()));
		UserAccount account = authenticationService.register(body);
		ApiResponseDTO<Account> response = new ApiResponseDTO<Account>("Đăng kí thành công", account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}

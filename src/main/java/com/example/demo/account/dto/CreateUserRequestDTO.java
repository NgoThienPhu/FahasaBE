package com.example.demo.account.dto;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.account.entity.UserAccount;
import com.example.demo.common.base.entity.PhoneNumber;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateUserRequestDTO(

		@NotBlank(message = "Tên đăng nhập không được để trống") 
		String username,

		@NotBlank(message = "Mật khẩu không được để trống") 
		String password,

		@NotBlank(message = "Họ và tên không được để trống") 
		String fullName,
		
		@Email(message = "Email không hợp lệ")
		String email,
		
		@NotBlank(message = "Mã otp không được để trống")
		String otpCode,

		@NotNull(message = "Số điện thoại không được để trống")
		@Pattern(regexp = "^(0|\\+84)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5]|9[0-9])\\d{7}$", message = "Số điện thoại không hợp lệ")
		String phoneNumber

) {
	
	public static UserAccount toUserAccount(CreateUserRequestDTO dto, PasswordEncoder passwordEncoder) {
		UserAccount userAccount = new UserAccount();
		userAccount.setUsername(dto.username());
		userAccount.setPassword(passwordEncoder.encode(dto.password()));
		userAccount.setFullName(dto.fullName());
		userAccount.setEmail(new com.example.demo.email.entity.Email(dto.email(), true));
		userAccount.setPhoneNumber(new PhoneNumber(dto.phoneNumber()));
		return userAccount;
	}
	
}

package com.example.demo.validator;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountValidator {
	
	@NotBlank(message = "Tên đăng nhập không được để trống")
	private String username;
	
	@NotBlank(message = "Mật khẩu không được để trống")
	private String password;
	
	@NotBlank(message = "Họ và tên không được để trống")
	private String fullName;
	
	@NotBlank(message = "Email không được để trống")
	@Email(message = "Email không hợp lệ")
	private  String email;
	
	@NotBlank(message = "Số điện thoại không được để trống")
	private  String phoneNumber;
	
}

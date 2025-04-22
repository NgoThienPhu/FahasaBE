package com.example.demo.validator;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginValidator {
	
	@NotBlank(message = "Tên tài khoản không được để trống")
	private String username;
	
	@NotBlank(message = "Mật khẩu không được để trống")
	private String password;
}

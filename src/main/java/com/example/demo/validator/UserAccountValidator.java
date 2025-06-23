package com.example.demo.validator;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserAccountValidator(

		@NotBlank(message = "Tên đăng nhập không được để trống") String username,

		@NotBlank(message = "Mật khẩu không được để trống") String password,

		@NotBlank(message = "Họ và tên không được để trống") String fullName,

		@NotBlank(message = "Email không được để trống") @Email(message = "Email không hợp lệ") String email,

		@NotBlank(message = "Số điện thoại không được để trống") String phoneNumber

) {
}

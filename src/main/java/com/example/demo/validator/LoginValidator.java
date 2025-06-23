package com.example.demo.validator;

import jakarta.validation.constraints.NotBlank;

public record LoginValidator(

		@NotBlank(message = "Tên tài khoản không được để trống") String username,

		@NotBlank(message = "Mật khẩu không được để trống") String password

) {
};

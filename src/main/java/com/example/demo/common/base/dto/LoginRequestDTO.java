package com.example.demo.common.base.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

		@NotBlank(message = "Tên tài khoản không được để trống") 
		String username,

		@NotBlank(message = "Mật khẩu không được để trống") 
		String password

) {
};

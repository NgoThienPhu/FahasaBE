package com.example.demo.common.base.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequestDTO(

		@NotBlank(message = "Mật khẩu cũ không được để trống") 
		String oldPassword,

		@NotBlank(message = "Mật khẩu mới không được để trống") 
		String newPassword

) {
}

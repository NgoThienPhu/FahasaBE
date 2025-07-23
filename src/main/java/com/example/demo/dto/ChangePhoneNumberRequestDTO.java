package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ChangePhoneNumberRequestDTO(

		@NotNull(message = "newPhoneNumber là trường bắt buộc")
		@Pattern(regexp = "^(0|\\+84)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5]|9[0-9])\\d{7}$", message = "Số điện thoại không hợp lệ") 
		String newPhoneNumber,

		@NotNull(message = "otp là trường bắt buộc")
		@Pattern(regexp = "^\\d{6}$", message = "Mã xác minh phải gồm đúng 6 chữ số") 
		String otp

) {
}

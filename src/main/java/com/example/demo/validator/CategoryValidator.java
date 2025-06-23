package com.example.demo.validator;

import jakarta.validation.constraints.NotBlank;

public record CategoryValidator(

		@NotBlank(message = "Tên loại sản phẩm không được để trống") String name

) {
}

package com.example.demo.validator;

import jakarta.validation.constraints.NotBlank;

public record AttributeValidator(

		@NotBlank(message = "Tên thuộc tính không được để trống") String attributeName

) {
}

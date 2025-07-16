package com.example.demo.validator;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryValidator(
		
		@NotBlank(message = "Tên loại sản phẩm không được để trống") 
		String categoryName
		
) {}

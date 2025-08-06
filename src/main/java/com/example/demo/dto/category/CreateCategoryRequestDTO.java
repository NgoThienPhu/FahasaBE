package com.example.demo.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequestDTO (

		@NotBlank(message = "Tên loại sản phẩm không được để trống") 
		String categoryName,
		
		String parentCategoryId
		
) {}

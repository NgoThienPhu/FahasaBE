package com.example.demo.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequestDTO (

		@NotBlank(message = "Tên loại sản phẩm con không được để trống") 
		String categoryName
		
) {}

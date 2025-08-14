package com.example.demo.category.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryNameRequestDTO(
		
		@NotBlank(message = "Tên loại sản phẩm không được để trống") 
		String categoryName
		
) {}

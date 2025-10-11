package com.example.demo.category.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryNameRequestDTO(
		
		@NotBlank(message = "Tên thể loại không được để trống")
		String categoryName
		
) {}

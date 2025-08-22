package com.example.demo.category.dto;

import jakarta.validation.constraints.Positive;

public record UpdateCategoryNameRequestDTO(
		
		String categoryName,
		
		@Positive(message = "Thứ tự sắp xếp luôn luôn phải lớn hơn 0")
		Integer sortOrder,
		
		Boolean visible
		
) {}

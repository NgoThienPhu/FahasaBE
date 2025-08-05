package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;

public record UpdateProductRequestDTO(

		String productName,

		String description,

		String categoryId,

		@DecimalMin(value = "0.0", inclusive = true, message = "Số lượng tối thiểu không được nhỏ hơn 0")
		Integer quantity

) {
}
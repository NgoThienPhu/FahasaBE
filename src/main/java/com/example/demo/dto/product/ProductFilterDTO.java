package com.example.demo.dto.product;

import java.math.BigDecimal;
import java.util.Map;

import jakarta.validation.constraints.DecimalMin;

public record ProductFilterDTO(

		String productName,

		String categoryId,

		@DecimalMin(value = "0.0", inclusive = true, message = "Giá tối thiểu không được nhỏ hơn 0")
		BigDecimal minPrice,

		@DecimalMin(value = "0.0", inclusive = true, message = "Giá tối đa không được nhỏ hơn 0")
		BigDecimal maxPrice,
		
		Map<String, String> attributes
		
) {}

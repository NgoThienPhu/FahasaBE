package com.example.demo.dto;

import java.math.BigDecimal;

public record UpdateProductRequestDTO(

		String productName,

		String description,

		String categoryId,

		BigDecimal price,

		Integer quantity

) {
}
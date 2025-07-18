package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ProductFilterDTO(

		String productName,

		String categoryId,

		BigDecimal minPrice,

		BigDecimal maxPrice,

		Map<String, String> attributes
		
) {}

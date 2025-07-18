package com.example.demo.dto;

import java.util.Map;

public record ProductFilterDTO(

		String productName,

		String categoryId,

		int minPrice,

		int maxPrice,

		Map<String, String> attributes
		
) {}

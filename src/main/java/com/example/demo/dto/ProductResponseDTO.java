package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.entities.AttributeValue;
import com.example.demo.entities.Category;
import com.example.demo.entities.ProductImage;
import com.example.demo.entities.SellPrice;
import com.example.demo.util.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.NoArgsConstructor;

public record ProductResponseDTO(
		
		@JsonView(View.Public.class)
		String productId,

		@JsonView(View.Public.class)
		String name,

		@JsonView(View.Public.class)
		String description,

		@JsonView(View.Public.class)
		Category category,
		
		@JsonView(View.Public.class)
		SellPrice sellPrice,

		@JsonView(View.Public.class)
		Integer quantity,

		@JsonView(View.Public.class)
		String skuCode,

		@JsonView(View.Public.class)
		List<ProductImage> images,

		@JsonView(View.Public.class)
		List<AttributeValue> attributeValues,

		@JsonView(View.Employee.class)
		LocalDateTime createdAt,

		@JsonView(View.Employee.class)
		LocalDateTime updatedAt
) {}


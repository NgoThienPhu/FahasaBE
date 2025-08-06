package com.example.demo.dto.product;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.entities.AttributeValue;
import com.example.demo.entities.Category;
import com.example.demo.entities.ProductImage;
import com.example.demo.entities.price.PromoPrice;
import com.example.demo.entities.price.SellPrice;
import com.example.demo.utils.view.View;
import com.fasterxml.jackson.annotation.JsonView;

public record ProductResponseDTO(
		
		@JsonView(View.Public.class)
		String productId,

		@JsonView(View.Public.class)
		String productName,

		@JsonView(View.Public.class)
		String productDescription,

		@JsonView(View.Public.class)
		Category category,
		
		@JsonView(View.Public.class)
		SellPrice sellPrice,
		
		@JsonView(View.Public.class)
		PromoPrice promoPrice,

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


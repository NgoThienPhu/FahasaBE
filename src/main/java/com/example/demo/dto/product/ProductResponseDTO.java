package com.example.demo.dto.product;

import java.util.List;

import com.example.demo.dto.attribute.AttributeResponseDTO;
import com.example.demo.dto.price.ProductPriceResponseDTO;
import com.example.demo.entities.ProductImage;

public record ProductResponseDTO(
		
		String productId,

		String productName,

		String productDescription,
		
		String categoryName,
		
		ProductPriceResponseDTO productPrice,
		
		List<ProductImage> images,

		List<AttributeResponseDTO> attributes
		
) {}


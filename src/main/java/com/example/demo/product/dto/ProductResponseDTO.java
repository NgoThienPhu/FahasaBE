package com.example.demo.product.dto;

import java.util.List;

import com.example.demo.attribute.dto.AttributeResponseDTO;
import com.example.demo.price.dto.ProductPriceResponseDTO;
import com.example.demo.productimage.entity.ProductImage;

public record ProductResponseDTO(
		
		String productId,

		String productName,

		String productDescription,
		
		String categoryName,
		
		ProductPriceResponseDTO productPrice,
		
		List<ProductImage> images,

		List<AttributeResponseDTO> attributes
		
) {}


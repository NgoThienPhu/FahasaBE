package com.example.demo.dto.product;

public record UpdateProductRequestDTO(

		String productName,

		String description,

		String categoryId

) {}
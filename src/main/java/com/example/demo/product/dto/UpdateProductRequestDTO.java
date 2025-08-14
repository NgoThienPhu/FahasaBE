package com.example.demo.product.dto;

public record UpdateProductRequestDTO(

		String productName,

		String description,

		String categoryId

) {}
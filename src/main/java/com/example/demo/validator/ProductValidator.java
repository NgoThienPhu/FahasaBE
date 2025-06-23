package com.example.demo.validator;

public record ProductValidator(

		String id,

		String name,

		String description,

		String categoryId,

		Double price,

		Integer quantity

) {
}

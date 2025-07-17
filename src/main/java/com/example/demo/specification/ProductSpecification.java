package com.example.demo.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entities.Product;

public class ProductSpecification {
	
	public static Specification<Product> hasCategory(String categoryId) {
		return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
	}
	
}

package com.example.demo.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entities.ProductImage;

public class ProductImageSpecification {
	
	public static Specification<ProductImage> hasProduct(String productId) {
		return (root, query, cb) -> cb.equal(root.get("id"), productId);
	}
	
	public static Specification<ProductImage> hasPrimaryImage() {
		return (root, query, cb) -> cb.isTrue(root.get("isPrimary"));
	}
	
	public static Specification<ProductImage> hasSecondaryImage() {
		return (root, query, cb) -> cb.isFalse(root.get("isPrimary"));
	}
	
}

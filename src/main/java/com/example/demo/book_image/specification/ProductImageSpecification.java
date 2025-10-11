package com.example.demo.book_image.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.book_image.entity.BookImage;

public class ProductImageSpecification {
	
	public static Specification<BookImage> hasProduct(String productId) {
		return (root, query, cb) -> cb.equal(root.get("id"), productId);
	}
	
	public static Specification<BookImage> hasPrimaryImage() {
		return (root, query, cb) -> cb.isTrue(root.get("isPrimary"));
	}
	
	public static Specification<BookImage> hasSecondaryImage() {
		return (root, query, cb) -> cb.isFalse(root.get("isPrimary"));
	}
	
}

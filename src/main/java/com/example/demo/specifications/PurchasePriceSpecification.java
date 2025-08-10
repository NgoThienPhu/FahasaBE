package com.example.demo.specifications;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entities.price.PurchasePrice;

public class PurchasePriceSpecification {

	public static Specification<PurchasePrice> hasProductId(String productId) {
		return (root, query, cb) -> cb.equal(root.get("product").get("id"), productId);
	}
	
	public static Specification<PurchasePrice> isActive() {
		return (root, query, cb) -> {
			LocalDateTime now = LocalDateTime.now();
			return cb.and(
				cb.lessThanOrEqualTo(root.get("startDate"), now),
				cb.isNull(root.get("endDate"))
			);
		};
	}
	
}

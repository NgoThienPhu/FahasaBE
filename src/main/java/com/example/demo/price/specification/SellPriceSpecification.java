package com.example.demo.price.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.price.entity.SellPrice;

public class SellPriceSpecification {
	
	public static Specification<SellPrice> isActive() {
		return (root, query, cb) -> {
			LocalDateTime now = LocalDateTime.now();
			return cb.and(
				cb.lessThanOrEqualTo(root.get("startDate"), now),
				cb.isNull(root.get("endDate"))
			);
		};
	}

	public static Specification<SellPrice> hasProductId(String productId) {
		return (root, query, cb) -> cb.equal(root.get("product").get("id"), productId);
	}
	
}

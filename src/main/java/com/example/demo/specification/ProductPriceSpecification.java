package com.example.demo.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entities.price.ProductPrice;

public class ProductPriceSpecification {
	
	public static Specification<ProductPrice> isActive() {
		return (root, query, cb) -> {
			LocalDateTime now = LocalDateTime.now();
			return cb.and(
					cb.lessThanOrEqualTo(root.get("startDate"), now),
					cb.or(
						cb.isNull(root.get("endDate")), 
						cb.greaterThan(root.get("endDate"), now)
					)
				);
		};
	}

	public static Specification<ProductPrice> hasProductId(String productId) {
		return (root, query, cb) -> cb.equal(root.get("product").get("productId"), productId);
	}
	
	public static Specification<ProductPrice> hasType(Class<? extends ProductPrice> type) {
		return (root, query, cb) -> cb.equal(root.type(), type);
	}
	
}

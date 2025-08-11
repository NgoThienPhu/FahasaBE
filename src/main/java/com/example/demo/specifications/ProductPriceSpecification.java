package com.example.demo.specifications;


import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entities.base.ProductPrice;

public class ProductPriceSpecification {
	
	public static Specification<ProductPrice> hasProductId(String productId) {
		return (root, query, cb) -> cb.equal(root.get("product").get("id"), productId);
	}
	
}

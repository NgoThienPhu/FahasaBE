package com.example.demo.product.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.attributevalue.entity.AttributeValue;
import com.example.demo.product.entity.Product;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

public class ProductSpecification {

	public static Specification<Product> hasCategoryId(String categoryId) {
		return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
	}

	public static Specification<Product> hasName(String productName) {
		return (root, query, cb) -> cb.like(root.get("name"), "%" + productName + "%");
	}

	public static Specification<Product> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
		return (root, query, cb) -> {
	        Predicate predicate = cb.conjunction();

	        if (minPrice != null) {
	            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("price"), minPrice));
	        }
	        if (maxPrice != null) {
	            predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("price"), maxPrice));
	        }

	        return predicate;
	    };
	}

	public static Specification<Product> hasAttribute(String attributeId, String value) {
		return (root, query, cb) -> {
			Join<Product, AttributeValue> join = root.join("attributeValues");
			return cb.and(
					cb.equal(join.get("attribute").get("id"), attributeId),
					cb.equal(join.get("value"), value)
			);
		};
	}

}

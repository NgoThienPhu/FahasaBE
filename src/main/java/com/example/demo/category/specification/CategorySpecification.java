package com.example.demo.category.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.category.entity.Category;

public class CategorySpecification {
	
	public static Specification<Category> hasId(String categoryId) {
		return (root, query, cb) -> cb.equal(root.get("id"), categoryId);
	}

	public static Specification<Category> hasName(String categoryName) {
		return (root, query, cb) -> cb.equal(cb.lower(root.get("name")), categoryName.toLowerCase());
	}

}

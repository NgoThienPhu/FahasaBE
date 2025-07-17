package com.example.demo.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entities.Category;

public class CategorySpecification {

	public static Specification<Category> nameEquals(String categoryName) {
		return (root, query, cb) -> cb.equal(root.get("name"), categoryName);
	}

	public static Specification<Category> hasName(String categoryName) {
		return (root, query, cb) -> cb.like(root.get("name"), "%" + categoryName + "%");
	}

	public static Specification<Category> hasChildren(String parentCategoryId) {
		return (root, query, cb) -> cb.equal(root.get("children").get("id"), parentCategoryId);
	}

}

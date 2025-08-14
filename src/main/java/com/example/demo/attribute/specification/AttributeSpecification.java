package com.example.demo.attribute.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.attribute.entity.Attribute;

public class AttributeSpecification {
	
	public static Specification<Attribute> hasName(String attributeName) {
		return (root, query, cb) -> {
			return cb.like(root.get("name"), "%" + attributeName + "%");
		};
	}
	
	public static Specification<Attribute> nameEquals(String attributeName) {
		return (root, query, cb) -> {
			return cb.equal(root.get("name"), attributeName);
		};
	}
	
}

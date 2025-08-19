package com.example.demo.attributevalue.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.attributevalue.entity.AttributeValue;

public class AttributeValueSpecification {

	public static Specification<AttributeValue> hasAttributeId(String attributeId) {
		return (root, query, cb) -> cb.equal(root.get("attribute").get("id"), attributeId);
	}

	public static Specification<AttributeValue> hasId(String attributeValueId) {
		return (root, query, cb) -> cb.equal(root.get("id"), attributeValueId);
	}

	public static Specification<AttributeValue> hasValueName(String attributeValue) {
		return (root, query, cb) -> cb.like(cb.lower(root.get("value")), "%" + attributeValue.toLowerCase() + "%");
	}

}

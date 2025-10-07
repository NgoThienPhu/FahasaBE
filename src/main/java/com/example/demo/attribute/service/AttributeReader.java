package com.example.demo.attribute.service;

import org.springframework.data.domain.Page;

import com.example.demo.attribute.entity.Attribute;

public interface AttributeReader {

	Page<Attribute> findAll(String attributeName, String orderBy, String sortBy, int page, int size);
	
	Attribute findById(String attributeId);
	
	Boolean existsByName(String categoryName);
	
}

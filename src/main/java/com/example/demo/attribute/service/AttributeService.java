package com.example.demo.attribute.service;

import org.springframework.data.domain.Page;

import com.example.demo.attribute.dto.CreateAttributeRequestDTO;
import com.example.demo.attribute.entity.Attribute;

public interface AttributeService {
	
	Page<Attribute> findAll(String attributeName, String orderBy, String sortBy, int page, int size);
	
	Attribute create(CreateAttributeRequestDTO body);
	
	Attribute findById(String attributeId);
	
	void deleteById(String attributeId);
	
	Attribute update(String attributeId, String attributeName);
	
	boolean existsName(String attributeName);
	
}

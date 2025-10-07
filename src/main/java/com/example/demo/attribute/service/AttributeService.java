package com.example.demo.attribute.service;

import com.example.demo.attribute.dto.CreateAttributeRequestDTO;
import com.example.demo.attribute.entity.Attribute;

public interface AttributeService extends AttributeReader {
	
	Attribute create(CreateAttributeRequestDTO body);
	
	void deleteById(String attributeId);
	
	Attribute update(String attributeId, String attributeName);
	
}

package com.example.demo.services.interfaces;

import org.springframework.data.domain.Page;

import com.example.demo.entities.Attribute;
import com.example.demo.validator.AttributeValidator;

public interface AttributeService {
	
	public Page<Attribute> getAttributes(String attributeName, String orderBy, String sortBy, int page, int size);
	
	public Attribute createAttribute(AttributeValidator body);
	
	public Attribute findById(String attributeId);
	
	public void deleteById(String attributeId);
	
	public Attribute updateAttribute(AttributeValidator body, String attributeId);
	
}

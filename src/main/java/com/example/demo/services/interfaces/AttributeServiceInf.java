package com.example.demo.services.interfaces;

import java.util.List;

import com.example.demo.entities.Attribute;
import com.example.demo.validator.AttributeValidator;

public interface AttributeServiceInf {
	
	public List<Attribute> getAttributes(String attributeName, String orderBy, String sortBy);
	
	public Attribute createAttribute(AttributeValidator body);
	
	public Attribute findById(String attributeId);
	
	public void deleteById(String attributeId);
	
	public Attribute updateAttribute(AttributeValidator body, String attributeId);
	
}

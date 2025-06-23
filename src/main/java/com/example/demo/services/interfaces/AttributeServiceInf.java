package com.example.demo.services.interfaces;

import com.example.demo.entities.Attribute;
import com.example.demo.validator.AttributeValidator;

public interface AttributeServiceInf {
	
	public Attribute createAttribute(AttributeValidator body);
	
	public Attribute findById(String attributeId);
	
	public void deleteById(String attributeId);
	
	public Attribute updateAttribute(AttributeValidator body, String attributeId);
	
}

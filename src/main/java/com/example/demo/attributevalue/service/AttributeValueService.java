package com.example.demo.attributevalue.service;

import java.util.List;

import com.example.demo.attribute.entity.Attribute;
import com.example.demo.attributevalue.entity.AttributeValue;

public interface AttributeValueService {

	AttributeValue findById(String attributeValueId);
	
	AttributeValue findByAttributeIdAndId(String attributeId, String attributeValueId);
	
	List<AttributeValue> findAllByAttributeId(String attributeId, String orderBy);
	
	boolean existsByAttributeIdAndValueName(String attributeId, String valueName);
	
	AttributeValue create(Attribute attribute, String value);
	
	void deleteByAttributeIdAndId(String attributeId, String attributeValueId);
	
}

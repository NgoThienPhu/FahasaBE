package com.example.demo.attribute.application;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.attribute.dto.CreateAttributeRequestDTO;
import com.example.demo.attribute.entity.Attribute;
import com.example.demo.attributevalue.entity.AttributeValue;

public interface AttributeApplicationService {
	
	Page<Attribute> findAll(String attributeName, String sortBy, String orderBy, int page, int size);
	
	Attribute findById(String attributeId);
	
	Attribute create(CreateAttributeRequestDTO dto);
	
	void deleteById(String attributeId);
	
	Attribute updateById(String attributeId, String attributeName);
	
	List<AttributeValue> findAllAttributeValue(String attributeId, String orderBy);
	
	AttributeValue findAttributeValueByAttributeById(String attributeId, String attributeValueId);
	
	AttributeValue createAttributeValue(String attributeId, String attributeValue);
	
	void deleteAttributeValueById(String attributeId, String attributeValueId);

}

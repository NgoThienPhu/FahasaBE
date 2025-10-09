package com.example.demo.attributevalue.service;

import java.util.List;

import com.example.demo.attributevalue.entity.AttributeValue;

public interface AttributeValueService {

	AttributeValue save(AttributeValue attributeValue);

	AttributeValue findById(String attributeValueId);

	AttributeValue findByAttributeIdAndId(String attributeId, String attributeValueId);

	List<AttributeValue> findAllByAttributeId(String attributeId, String orderBy);

	boolean existsByAttributeIdAndValueName(String attributeId, String valueName);

	void deleteByAttributeIdAndId(String attributeId, String attributeValueId);

}

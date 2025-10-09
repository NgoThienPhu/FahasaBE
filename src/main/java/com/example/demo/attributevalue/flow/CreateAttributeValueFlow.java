package com.example.demo.attributevalue.flow;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.attribute.entity.Attribute;
import com.example.demo.attribute.service.AttributeService;
import com.example.demo.attributevalue.entity.AttributeValue;
import com.example.demo.attributevalue.service.AttributeValueService;

@Service
public class CreateAttributeValueFlow {
	
	private AttributeValueService attributeValueService;
	
	private AttributeService attributeService;
	
	public CreateAttributeValueFlow(AttributeValueService attributeValueService, AttributeService attributeService) {
		this.attributeValueService = attributeValueService;
		this.attributeService = attributeService;
	}

	public AttributeValue createAttributeValue(String attributeId, String value) {
		boolean existsValue = attributeValueService.existsByAttributeIdAndValueName(attributeId, value);

		if (existsValue)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					String.format("Giá trị %s đã tồn tại", value));
		
		Attribute attribute = attributeService.findById(attributeId);
		AttributeValue attributeValue = new AttributeValue(attribute, value);
		
		return attributeValueService.save(attributeValue);
	}
	
}

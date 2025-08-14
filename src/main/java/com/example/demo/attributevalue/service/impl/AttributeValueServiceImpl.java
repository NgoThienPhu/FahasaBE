package com.example.demo.attributevalue.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.attributevalue.entity.AttributeValue;
import com.example.demo.attributevalue.repository.AttributeValueRepository;
import com.example.demo.attributevalue.service.AttributeValueService;

@Service
public class AttributeValueServiceImpl implements AttributeValueService {
	
	private AttributeValueRepository attributeValueRepository;
	
	public AttributeValueServiceImpl(AttributeValueRepository attributeValueRepository) {
		this.attributeValueRepository = attributeValueRepository;
	}

	@Override
	public AttributeValue findById(String attributeValueId) {
		return attributeValueRepository.findById(attributeValueId).orElse(null);
	}

}

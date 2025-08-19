package com.example.demo.attribute.application.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.demo.attribute.application.AttributeApplicationService;
import com.example.demo.attribute.dto.CreateAttributeRequestDTO;
import com.example.demo.attribute.entity.Attribute;
import com.example.demo.attribute.service.AttributeService;
import com.example.demo.attributevalue.entity.AttributeValue;
import com.example.demo.attributevalue.service.AttributeValueService;

@Service
public class AttributeApplicationServiceImpl implements AttributeApplicationService {
	
	private AttributeService attributeService;
	
	private AttributeValueService attributeValueService;

	public AttributeApplicationServiceImpl(AttributeService attributeService,
			AttributeValueService attributeValueService) {
		this.attributeService = attributeService;
		this.attributeValueService = attributeValueService;
	}

	@Override
	public Attribute create(CreateAttributeRequestDTO dto) {
		return attributeService.create(dto);
	}

	@Override
	public Page<Attribute> findAll(String attributeName, String sortBy, String orderBy, int page, int size) {
		return attributeService.findAll(attributeName, orderBy, sortBy, page, size);
	}

	@Override
	public Attribute findById(String attributeId) {
		return attributeService.findById(attributeId);
	}

	@Override
	public void deleteById(String attributeId) {
		attributeService.deleteById(attributeId);
	}

	@Override
	public Attribute updateById(String attributeId, String attributeName) {
		return attributeService.update(attributeId, attributeName);
	}

	@Override
	public List<AttributeValue> findAllAttributeValue(String attributeId, String orderBy) {
		return attributeValueService.findAllByAttributeId(attributeId, orderBy);
	}

	@Override
	public AttributeValue findAttributeValueByAttributeById(String attributeId, String attributeValueId) {
		return attributeValueService.findByAttributeIdAndId(attributeId, attributeValueId);
	}

	@Override
	public AttributeValue createAttributeValue(String attributeId, String attributeValue) {
		Attribute attribute = attributeService.findById(attributeId);
		return attributeValueService.create(attribute, attributeValue);
	}

	@Override
	public void deleteAttributeValueById(String attributeId, String attributeValueId) {
		attributeValueService.deleteByAttributeIdAndId(attributeId, attributeValueId);
	}
	
	
	
}

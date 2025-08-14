package com.example.demo.attributevalue.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
		return attributeValueRepository.findById(attributeValueId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("Không tìm thấy giá trị thuộc tính với Id là: %s", attributeValueId)));
	}

}

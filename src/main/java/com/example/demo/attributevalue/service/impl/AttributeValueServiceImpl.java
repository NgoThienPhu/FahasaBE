package com.example.demo.attributevalue.service.impl;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.attribute.entity.Attribute;
import com.example.demo.attributevalue.entity.AttributeValue;
import com.example.demo.attributevalue.repository.AttributeValueRepository;
import com.example.demo.attributevalue.service.AttributeValueService;
import com.example.demo.attributevalue.specification.AttributeValueSpecification;

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

	@Override
	public List<AttributeValue> findAllByAttributeId(String attributeId, String orderBy) {
		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, "value")
				: Sort.by(Sort.Direction.DESC, "value");
		return attributeValueRepository.findAll(AttributeValueSpecification.hasAttributeId(attributeId), sort);
	}

	@Override
	public AttributeValue findByAttributeIdAndId(String attributeId, String attributeValueId) {
		Specification<AttributeValue> spec = Specification
				.where(AttributeValueSpecification.hasAttributeId(attributeId))
				.and(AttributeValueSpecification.hasId(attributeValueId));

		return attributeValueRepository.findOne(spec).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy giá trị của thuộc tính"));
	}

	@Override
	public boolean existsByAttributeIdAndValueName(String attributeId, String valueName) {
		Specification<AttributeValue> spec = Specification
				.where(AttributeValueSpecification.hasAttributeId(attributeId))
				.and(AttributeValueSpecification.hasValueName(valueName));
		return attributeValueRepository.count(spec) > 0;
	}

	@Override
	public AttributeValue create(Attribute attribute, String value) {
		boolean existsValue = existsByAttributeIdAndValueName(attribute.getId(), value);

		if (existsValue)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					String.format("Giá trị %s đã tồn tại", value));
		
		AttributeValue attributeValue = new AttributeValue(attribute, value);
		
		return attributeValueRepository.save(attributeValue);
	}

	@Override
	public void deleteByAttributeIdAndId(String attributeId, String attributeValueId) {
		Specification<AttributeValue> spec = Specification
				.where(AttributeValueSpecification.hasAttributeId(attributeId))
				.and(AttributeValueSpecification.hasId(attributeValueId));
		attributeValueRepository.delete(spec);
	}

}

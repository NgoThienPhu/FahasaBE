package com.example.demo.attribute.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.attribute.dto.CreateAttributeRequestDTO;
import com.example.demo.attribute.entity.Attribute;
import com.example.demo.attribute.repository.AttributeRepository;
import com.example.demo.attribute.service.AttributeService;
import com.example.demo.attribute.specification.AttributeSpecification;

import jakarta.transaction.Transactional;

@Service
public class AttributeServiceImpl implements AttributeService {

	private AttributeRepository attributeRepository;

	public AttributeServiceImpl(AttributeRepository attributeRepository) {
		this.attributeRepository = attributeRepository;
	}
	
	@Override
	public Page<Attribute> findAll(String attributeName, String orderBy, String sortBy, int page, int size) {
		List<String> allowedFields = List.of("name");
		
		if (!allowedFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}

		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);

		Specification<Attribute> spec = Specification.where(null);
		if (attributeName != null && !attributeName.isBlank()) {
			spec = spec.and(AttributeSpecification.hasName(attributeName));
		}

		return attributeRepository.findAll(spec, pageable);
	}
	
	@Override
	public Attribute findById(String attributeId) {
		Attribute attriubte = attributeRepository.findById(attributeId).orElse(null);
		if (attriubte == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Không tìm thấy attribute với Id là: %s", attributeId));
		return attriubte;
	}

	@Transactional
	@Override
	public Attribute create(CreateAttributeRequestDTO dto) {
		boolean checkNameExists = existsName(dto.attributeName());
		if (checkNameExists)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Tên thuộc tính đã tồn tại vui lòng chọn tên khác");
		Attribute attribute = new Attribute();
		attribute.setName(dto.attributeName());
		return attributeRepository.save(attribute);
	}
	
	@Transactional
	@Override
	public Attribute update(CreateAttributeRequestDTO body, String attributeId) {
		Attribute attribute = findById(attributeId);
		if (attribute == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Không tìm thấy thuộc tính với id là: %s", attributeId));
		attribute.setName(body.attributeName());
		return attributeRepository.save(attribute);
	}

	@Transactional
	@Override
	public void deleteById(String attributeId) {
		attributeRepository.deleteById(attributeId);
	}

	@Override
	public boolean existsName(String categoryName) {
		Specification<Attribute> spec = AttributeSpecification.hasName(categoryName);
		return attributeRepository.count(spec) > 0;
	}

}

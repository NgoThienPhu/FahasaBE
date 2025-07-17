package com.example.demo.services.implement;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.Attribute;
import com.example.demo.repository.AttributeRepository;
import com.example.demo.services.interfaces.AttributeService;
import com.example.demo.specification.AttributeSpecification;
import com.example.demo.validator.AttributeValidator;

import jakarta.transaction.Transactional;

@Service
public class AttributeServiceImpl implements AttributeService {

	private AttributeRepository attributeRepository;

	public AttributeServiceImpl(AttributeRepository attributeRepository) {
		this.attributeRepository = attributeRepository;
	}

	@Transactional
	@Override
	public Attribute createAttribute(AttributeValidator body) {
		Specification<Attribute> spec = AttributeSpecification.hasName(body.attributeName());
		Boolean checkNameExists = attributeRepository.count(spec) > 0;
		if (checkNameExists)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Tên thuộc tính đã tồn tại vui lòng chọn tên khác");
		Attribute attribute = new Attribute();
		attribute.setName(body.attributeName());
		return attributeRepository.save(attribute);
	}

	@Override
	public Attribute findById(String attributeId) {
		Attribute attriubte = attributeRepository.findById(attributeId).orElse(null);
		if (attriubte == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Không tìm thấy attribute với id là: %s", attributeId));
		return attriubte;
	}

	@Transactional
	@Override
	public void deleteById(String attributeId) {
		Attribute attriubte = attributeRepository.findById(attributeId).orElse(null);
		if (attriubte == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Không tìm thấy attribute với id là: %s", attributeId));
		attributeRepository.delete(attriubte);
	}

	@Transactional
	@Override
	public Attribute updateAttribute(AttributeValidator body, String attributeId) {
		Attribute attribute = attributeRepository.findById(attributeId).orElse(null);
		if (attribute == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Không tìm thấy attribute với id là: %s", attributeId));
		attribute.setName(body.attributeName());
		return attributeRepository.save(attribute);
	}

	@Override
	public Page<Attribute> getAttributes(String attributeName, String orderBy, String sortBy, int page, int size) {
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

}

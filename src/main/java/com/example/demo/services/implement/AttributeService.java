package com.example.demo.services.implement;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.Attribute;
import com.example.demo.repository.AttributeRepository;
import com.example.demo.services.interfaces.AttributeServiceInf;
import com.example.demo.validator.AttributeValidator;

import jakarta.transaction.Transactional;

@Service
public class AttributeService implements AttributeServiceInf {
	
	private AttributeRepository attributeRepository;

	public AttributeService(AttributeRepository attributeRepository) {
		this.attributeRepository = attributeRepository;
	}

	@Transactional
	@Override
	public Attribute createAttribute(AttributeValidator body) {
		Attribute attribute = new Attribute();
		attribute.setName(body.attributeName());
		return attributeRepository.save(attribute);
	}

	@Override
	public Attribute findById(String attributeId) {
		Attribute attriubte = attributeRepository.findById(attributeId).orElse(null);
		if(attriubte == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Không tìm thấy attribute với id là: %s", attributeId));
		return attriubte;
	}
	
	@Transactional
	@Override
	public void deleteById(String attributeId) {
		Attribute attriubte = attributeRepository.findById(attributeId).orElse(null);
		if(attriubte == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Không tìm thấy attribute với id là: %s", attributeId));
		attributeRepository.delete(attriubte);
	}
	
	@Transactional
	@Override
	public Attribute updateAttribute(AttributeValidator body, String attributeId) {
		Attribute attribute = attributeRepository.findById(attributeId).orElse(null);
		if(attribute == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Không tìm thấy attribute với id là: %s", attributeId));
		attribute.setName(body.attributeName());
		return attributeRepository.save(attribute);
	}
	
}

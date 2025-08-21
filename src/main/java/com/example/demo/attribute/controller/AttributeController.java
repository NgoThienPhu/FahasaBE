package com.example.demo.attribute.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.attribute.application.AttributeApplicationService;
import com.example.demo.common.base.dto.ApiResponseDTO;
import com.example.demo.common.base.dto.PagedResponseDTO;

@RestController
@RequestMapping("/api/attributes")
public class AttributeController {

	private AttributeApplicationService attributeApplicationService;

	public AttributeController(AttributeApplicationService attributeApplicationService) {
		this.attributeApplicationService = attributeApplicationService;
	}

	@GetMapping
	public ResponseEntity<?> findAll(@RequestParam(required = false) String attributeName,
			@RequestParam(required = true, defaultValue = "name") String sortBy,
			@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		var attributes = attributeApplicationService.findAll(attributeName, sortBy, orderBy, page, size);
		var pagedResponseDTO = PagedResponseDTO.convertPageToPagedResponseDTO(attributes);
		var response = new ApiResponseDTO<>("Tìm thuộc tính thành công", "success", pagedResponseDTO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{attributeId}")
	public ResponseEntity<?> findById(@PathVariable String attributeId) {
		var attribute = attributeApplicationService.findById(attributeId);
		var response = new ApiResponseDTO<>("Tìm thuộc tính thành công", "success", attribute);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}

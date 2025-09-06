package com.example.demo.attributevalue.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.attribute.application.AttributeApplicationService;
import com.example.demo.attributevalue.entity.AttributeValue;
import com.example.demo.common.base.dto.ApiResponseDTO;

@RestController
@RequestMapping("/api/attributes/{attributeId}/attribute-values")
public class AttributeValueController {

	private AttributeApplicationService attributeApplicationService;

	public AttributeValueController(AttributeApplicationService attributeApplicationService) {
		this.attributeApplicationService = attributeApplicationService;
	}

	@GetMapping
	public ResponseEntity<?> findAll(@PathVariable String attributeId,
			@RequestParam(defaultValue = "asc") String orderBy) {
		List<AttributeValue> attributeValues = attributeApplicationService.findAllAttributeValue(attributeId, orderBy);
		var response = new ApiResponseDTO<List<AttributeValue>>("Lấy danh sách giá trị thuộc tính thành công",
				true, attributeValues);
		return new ResponseEntity<ApiResponseDTO<List<AttributeValue>>>(response, HttpStatus.OK);
	}

	@GetMapping("/{attributeValueId}")
	public ResponseEntity<?> findById(@PathVariable String attributeId, @PathVariable String attributeValueId) {
		AttributeValue attributeValue = attributeApplicationService.findAttributeValueByAttributeById(attributeId,
				attributeValueId);
		var response = new ApiResponseDTO<AttributeValue>("Lấy giá trị thuộc tính thành công", true,
				attributeValue);
		return new ResponseEntity<ApiResponseDTO<AttributeValue>>(response, HttpStatus.OK);
	}

}

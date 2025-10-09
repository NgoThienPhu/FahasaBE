package com.example.demo.attributevalue.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.attributevalue.entity.AttributeValue;
import com.example.demo.attributevalue.flow.CreateAttributeValueFlow;
import com.example.demo.attributevalue.service.AttributeValueService;
import com.example.demo.util.base.dto.ApiResponseDTO;

@RestController
@RequestMapping("/api/admin/attributes/{attributeId}/attribute-values")
public class AdminAttributeValueController {

	private AttributeValueService attributeValueService;

	private CreateAttributeValueFlow createAttributeValueFlow;

	public AdminAttributeValueController(AttributeValueService attributeValueService,
			CreateAttributeValueFlow createAttributeValueFlow) {
		this.attributeValueService = attributeValueService;
		this.createAttributeValueFlow = createAttributeValueFlow;
	}

	@PostMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> create(@PathVariable String attributeId, @RequestParam String attributeValue) {
		AttributeValue attributev = createAttributeValueFlow.createAttributeValue(attributeId, attributeValue);
		var response = new ApiResponseDTO<AttributeValue>("Thêm giá trị thuộc tính thành công", true, attributev);
		return new ResponseEntity<ApiResponseDTO<AttributeValue>>(response, HttpStatus.OK);
	}

	@DeleteMapping("{attributeValueId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> delete(@PathVariable String attributeId, @PathVariable String attributeValueId) {
		attributeValueService.deleteByAttributeIdAndId(attributeId, attributeValueId);
		var response = new ApiResponseDTO<AttributeValue>("Xóa giá trị thuộc tính thành công", true);
		return new ResponseEntity<ApiResponseDTO<AttributeValue>>(response, HttpStatus.OK);
	}

}

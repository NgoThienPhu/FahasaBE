package com.example.demo.attributevalue.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.attribute.application.AttributeApplicationService;
import com.example.demo.attributevalue.entity.AttributeValue;
import com.example.demo.common.base.dto.ApiResponseDTO;

@RestController
@RequestMapping("/api/admin/attributes/{attributeId}/attribute-values")
public class AdminAttributeValueController {

	private AttributeApplicationService attributeApplicationService;

	public AdminAttributeValueController(AttributeApplicationService attributeApplicationService) {
		this.attributeApplicationService = attributeApplicationService;
	}

	@PostMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> create(@PathVariable String attributeId, @RequestParam String attributeValue) {
		AttributeValue attributev = attributeApplicationService.createAttributeValue(attributeId, attributeValue);
		var response = new ApiResponseDTO<AttributeValue>("Thêm giá trị thuộc tính thành công", "success", attributev);
		return new ResponseEntity<ApiResponseDTO<AttributeValue>>(response, HttpStatus.OK);
	}

	@DeleteMapping("{attributeValueId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> delete(@PathVariable String attributeId, @PathVariable String attributeValueId) {
		attributeApplicationService.deleteAttributeValueById(attributeId, attributeValueId);
		var response = new ApiResponseDTO<AttributeValue>("Xóa giá trị thuộc tính thành công", "success");
		return new ResponseEntity<ApiResponseDTO<AttributeValue>>(response, HttpStatus.OK);
	}

}

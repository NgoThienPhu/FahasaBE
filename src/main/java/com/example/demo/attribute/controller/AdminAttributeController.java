package com.example.demo.attribute.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.attribute.application.AttributeApplicationService;
import com.example.demo.attribute.dto.CreateAttributeRequestDTO;
import com.example.demo.attribute.entity.Attribute;
import com.example.demo.common.base.dto.ApiResponseDTO;
import com.example.demo.common.validation.BindingResultUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/attributes")
public class AdminAttributeController {

	private AttributeApplicationService attributeApplicationService;

	public AdminAttributeController(AttributeApplicationService attributeApplicationService) {
		this.attributeApplicationService = attributeApplicationService;
	}

	@PostMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createAttribute(@Valid @RequestBody CreateAttributeRequestDTO body, BindingResult result) {

		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result,
				"Tạo thuộc tính sản phẩm thất bại!");
		if (responseError != null)
			return responseError;

		Attribute attribute = attributeApplicationService.create(body);
		ApiResponseDTO<Attribute> response = new ApiResponseDTO<Attribute>("Tạo thuộc tính thành công", "success",
				attribute);
		return new ResponseEntity<ApiResponseDTO<Attribute>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/{attributeId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteById(@PathVariable String attributeId) {
		attributeApplicationService.deleteById(attributeId);
		ApiResponseDTO<Attribute> response = new ApiResponseDTO<Attribute>("Xóa thuộc tính thành công", "success");
		return new ResponseEntity<ApiResponseDTO<Attribute>>(response, HttpStatus.OK);
	}

	@PatchMapping("/{attributeId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateById(@Valid @RequestBody CreateAttributeRequestDTO body, BindingResult result,
			@PathVariable String attributeId) {

		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result,
				"Cập nhật thuộc tính thất bại!");
		if (responseError != null)
			return responseError;

		Attribute attribute = attributeApplicationService.updateById(attributeId, body.attributeName());
		ApiResponseDTO<Attribute> response = new ApiResponseDTO<Attribute>("Cập nhật thuộc tính thành công", "success",
				attribute);
		return new ResponseEntity<ApiResponseDTO<Attribute>>(response, HttpStatus.OK);
	}
}

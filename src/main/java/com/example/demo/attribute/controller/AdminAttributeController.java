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

import com.example.demo.attribute.dto.CreateAttributeRequestDTO;
import com.example.demo.attribute.entity.Attribute;
import com.example.demo.attribute.service.AttributeService;
import com.example.demo.util.base.dto.ApiResponseDTO;
import com.example.demo.util.validation.BindingResultUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/attributes")
public class AdminAttributeController {

	private AttributeService attributeService;

	public AdminAttributeController(AttributeService attributeService) {
		this.attributeService = attributeService;
	}

	@PostMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createAttribute(@Valid @RequestBody CreateAttributeRequestDTO body, BindingResult result) {

		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result,
				"Tạo thuộc tính sản phẩm thất bại!");
		if (responseError != null)
			return responseError;

		Attribute attribute = attributeService.create(body);
		var response = new ApiResponseDTO<Attribute>("Tạo thuộc tính thành công", true, attribute);
		return new ResponseEntity<ApiResponseDTO<Attribute>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/{attributeId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteById(@PathVariable String attributeId) {
		attributeService.deleteById(attributeId);
		var response = new ApiResponseDTO<Void>("Xóa thuộc tính thành công", true);
		return new ResponseEntity<ApiResponseDTO<Void>>(response, HttpStatus.OK);
	}

	@PatchMapping("/{attributeId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateById(@Valid @RequestBody CreateAttributeRequestDTO body, BindingResult result,
			@PathVariable String attributeId) {

		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result,
				"Cập nhật thuộc tính thất bại!");
		if (responseError != null)
			return responseError;

		Attribute attribute = attributeService.update(attributeId, body.attributeName());
		var response = new ApiResponseDTO<Attribute>("Cập nhật thuộc tính thành công", true, attribute);
		return new ResponseEntity<ApiResponseDTO<Attribute>>(response, HttpStatus.OK);
	}
}

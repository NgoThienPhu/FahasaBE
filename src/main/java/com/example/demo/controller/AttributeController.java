package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponseDTO;
import com.example.demo.entities.Attribute;
import com.example.demo.services.interfaces.AttributeServiceInf;
import com.example.demo.utils.BindingResultUtils;
import com.example.demo.validator.AttributeValidator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/attributes")
public class AttributeController {

	private AttributeServiceInf attributeService;

	public AttributeController(AttributeServiceInf attributeServiceInf) {
		this.attributeService = attributeServiceInf;
	}

	@GetMapping("/{attributeId}")
	public ResponseEntity<?> findById(@PathVariable String attributeId) {
		Attribute attribute = attributeService.findById(attributeId);
		ApiResponseDTO<Attribute> response = new ApiResponseDTO<Attribute>("Tìm thuộc tính thành công", "success",
				attribute);
		return new ResponseEntity<ApiResponseDTO<Attribute>>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> createAttribute(@Valid @RequestBody AttributeValidator body, BindingResult result) {

		ResponseEntity<?> responseError = BindingResultUtils.handleValidationErrors(result,
				"Tạo thuộc tính sản phẩm thất bại!");
		if (responseError != null)
			return responseError;

		Attribute attribute = attributeService.createAttribute(body);
		ApiResponseDTO<Attribute> response = new ApiResponseDTO<Attribute>("Tạo thuộc tính thành công", "success",
				attribute);
		return new ResponseEntity<ApiResponseDTO<Attribute>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/{attributeId}")
	public ResponseEntity<?> deleteById(@PathVariable String attributeId) {
		attributeService.deleteById(attributeId);
		ApiResponseDTO<Attribute> response = new ApiResponseDTO<Attribute>("Xóa thuộc tính thành công", "success");
		return new ResponseEntity<ApiResponseDTO<Attribute>>(response, HttpStatus.OK);
	}

	@PatchMapping("/{attributeId}")
	public ResponseEntity<?> updateById(@Valid @RequestBody AttributeValidator body, BindingResult result,
			@PathVariable String attributeId) {

		ResponseEntity<?> responseError = BindingResultUtils.handleValidationErrors(result,
				"Cập nhật thuộc tính thất bại!");
		if (responseError != null)
			return responseError;

		Attribute attribute = attributeService.updateAttribute(body, attributeId);
		ApiResponseDTO<Attribute> response = new ApiResponseDTO<Attribute>("Cập nhật thuộc tính thành công", "success",
				attribute);
		return new ResponseEntity<ApiResponseDTO<Attribute>>(response, HttpStatus.OK);
	}
}

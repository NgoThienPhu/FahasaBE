package com.example.demo.controllers.admin;

import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.attribute.CreateAttributeRequestDTO;
import com.example.demo.dto.base.ApiResponseDTO;
import com.example.demo.dto.base.PagedResponseDTO;
import com.example.demo.entities.Attribute;
import com.example.demo.services.interfaces.AttributeService;
import com.example.demo.utils.validation.BindingResultUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/attributes")
public class AdminAttributeController {

	private AttributeService attributeService;

	public AdminAttributeController(AttributeService attributeService) {
		this.attributeService = attributeService;
	}

	@GetMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAttributes(@RequestParam(required = false) String attributeName,
			@RequestParam(required = true, defaultValue = "name") String sortBy,
			@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<Attribute> attributes = attributeService.getAttributes(attributeName, orderBy, sortBy, page, size);
		PagedResponseDTO<Attribute> pagedResponseDTO = PagedResponseDTO.convertPageToPagedResponseDTO(attributes);
		ApiResponseDTO<PagedResponseDTO<Attribute>> response = new ApiResponseDTO<PagedResponseDTO<Attribute>>(
				"Tìm thuộc tính thành công", "success", pagedResponseDTO);
		return new ResponseEntity<ApiResponseDTO<PagedResponseDTO<Attribute>>>(response, HttpStatus.OK);
	}

	@GetMapping("/{attributeId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> findById(@PathVariable String attributeId) {
		Attribute attribute = attributeService.findById(attributeId);
		ApiResponseDTO<Attribute> response = new ApiResponseDTO<Attribute>("Tìm thuộc tính thành công", "success",
				attribute);
		return new ResponseEntity<ApiResponseDTO<Attribute>>(response, HttpStatus.OK);
	}

	@PostMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createAttribute(@Valid @RequestBody CreateAttributeRequestDTO body, BindingResult result) {

		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result,
				"Tạo thuộc tính sản phẩm thất bại!");
		if (responseError != null)
			return responseError;

		Attribute attribute = attributeService.createAttribute(body);
		ApiResponseDTO<Attribute> response = new ApiResponseDTO<Attribute>("Tạo thuộc tính thành công", "success",
				attribute);
		return new ResponseEntity<ApiResponseDTO<Attribute>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/{attributeId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteById(@PathVariable String attributeId) {
		attributeService.deleteById(attributeId);
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

		Attribute attribute = attributeService.updateAttribute(body, attributeId);
		ApiResponseDTO<Attribute> response = new ApiResponseDTO<Attribute>("Cập nhật thuộc tính thành công", "success",
				attribute);
		return new ResponseEntity<ApiResponseDTO<Attribute>>(response, HttpStatus.OK);
	}
}

package com.example.demo.category.controller;

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

import com.example.demo.category.dto.CreateCategoryRequestDTO;
import com.example.demo.category.dto.UpdateCategoryNameRequestDTO;
import com.example.demo.category.entity.Category;
import com.example.demo.category.service.CategoryService;
import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponseSuccessDTO;
import com.example.demo.util.validation.BindingResultUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

	private CategoryService categoryService;

	public AdminCategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@PostMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createCategoryRequestDTO(@Valid @RequestBody CreateCategoryRequestDTO body,
			HttpServletRequest request, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result,
				"Tạo mới loại sản phẩm thất bại!", request.getRequestURI());
		if (responseError != null)
			return responseError;

		Category myCategory = categoryService.create(body);
		var response = new ApiResponseSuccessDTO<Category>(200, "Tạo loại sản phẩm thành công", myCategory);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@PatchMapping("/{categoryId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateCategoryName(@PathVariable String categoryId,
			@Valid @RequestBody UpdateCategoryNameRequestDTO updateCategoryNameRequestDTO, HttpServletRequest request,
			BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result,
				"Cập nhật loại sản phẩm thất bại!", request.getRequestURI());
		if (responseError != null)
			return responseError;
		Category category = categoryService.update(updateCategoryNameRequestDTO, categoryId);
		var response = new ApiResponseSuccessDTO<Category>(200, "Cập nhật loại sản phẩm thành công", category);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{categoryId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteCategory(@PathVariable String categoryId) {
		categoryService.deleteById(categoryId);
		var response = new ApiResponseSuccessDTO<Void>(200, "Xoá thành công");
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

}

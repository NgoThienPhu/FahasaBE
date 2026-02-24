package com.example.demo.category.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.category.dto.CreateCategoryRequestDTO;
import com.example.demo.category.dto.UpdateCategoryNameRequestDTO;
import com.example.demo.category.entity.Category;
import com.example.demo.category.service.CategoryService;
import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponsePaginationSuccess;
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

	@GetMapping
	public ResponseEntity<?> getAll(@RequestParam(required = false) String search,
			@RequestParam(defaultValue = "asc") String orderBy, @RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Page<Category> categories = categoryService.findAll(search ,orderBy, sortBy, page, size);
		ApiResponsePaginationSuccess<List<Category>> response = ApiResponsePaginationSuccess.fromPage(categories,
				"Lấy danh sách loại sản phẩm thành công");
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<?> findById(@PathVariable String categoryId) {
		Category category = categoryService.findById(categoryId);
		var response = new ApiResponseSuccessDTO<Category>(200, "Tìm kiếm thành công", category);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody CreateCategoryRequestDTO body, HttpServletRequest request,
			BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result,
				"Tạo mới loại sản phẩm thất bại!", request.getRequestURI());
		if (responseError != null)
			return responseError;

		Category myCategory = categoryService.create(body);
		var response = new ApiResponseSuccessDTO<Category>(200, "Tạo loại sản phẩm thành công", myCategory);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@PutMapping("/{categoryId}")
	public ResponseEntity<?> update(@PathVariable String categoryId,
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
	public ResponseEntity<?> delete(@PathVariable String categoryId) {
		categoryService.delete(categoryId);
		var response = new ApiResponseSuccessDTO<String>(200, "Xóa loại sản phẩm thành công", categoryId);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

}

package com.example.demo.category.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.category.entity.Category;
import com.example.demo.category.service.CategoryService;
import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponsePaginationSuccess;
import com.example.demo.util.dto.api_response.ApiResponseSuccessDTO;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	private CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public ResponseEntity<?> getAll(@RequestParam(required = false) String search,
			@RequestParam(defaultValue = "asc") String orderBy, 
			@RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int size) {
		Page<Category> categories = categoryService.findAll(search, orderBy, sortBy, page, size);
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
}

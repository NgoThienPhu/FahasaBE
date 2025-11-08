package com.example.demo.category.controller;

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
import com.example.demo.util.dto.ApiResponseDTO;
import com.example.demo.util.dto.PagedResponseDTO;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	private CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public ResponseEntity<?> getAll(@RequestParam(required = false) String categoryName,
			@RequestParam(defaultValue = "asc") String orderBy, @RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		Page<Category> categories = categoryService.findAll(orderBy, sortBy, page, size);
		PagedResponseDTO<Category> pagedResponseDTO = PagedResponseDTO.convertPageToPagedResponseDTO(categories);
		var response = new ApiResponseDTO<PagedResponseDTO<Category>>("Lấy danh sách loại sản phẩm thành công", true,
				pagedResponseDTO);
		return new ResponseEntity<ApiResponseDTO<PagedResponseDTO<Category>>>(response, HttpStatus.OK);
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<?> findById(@PathVariable String categoryId) {
		Category category = categoryService.findById(categoryId);
		var response = new ApiResponseDTO<Category>("Tìm loại sản phẩm thành công", true, category);
		return new ResponseEntity<ApiResponseDTO<Category>>(response, HttpStatus.OK);
	}
}

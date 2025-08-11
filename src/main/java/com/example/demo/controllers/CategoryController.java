package com.example.demo.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.base.ApiResponseDTO;
import com.example.demo.dto.base.PagedResponseDTO;
import com.example.demo.entities.Category;
import com.example.demo.services.interfaces.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	private CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public ResponseEntity<?> getCategories(@RequestParam(required = false) String categoryName,
			@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "name") String sortBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<Category> categories = categoryService.findAll(categoryName, orderBy, sortBy, page, size);
		PagedResponseDTO<Category> pagedResponseDTO = PagedResponseDTO.convertPageToPagedResponseDTO(categories);
		ApiResponseDTO<PagedResponseDTO<Category>> response = new ApiResponseDTO<PagedResponseDTO<Category>>(
				"Lấy danh sách loại sản phẩm thành công", "success", pagedResponseDTO);
		return new ResponseEntity<ApiResponseDTO<PagedResponseDTO<Category>>>(response, HttpStatus.OK);
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<?> findCategoryById(@PathVariable String categoryId) {
		Category category = categoryService.findById(categoryId);
		ApiResponseDTO<Category> response = new ApiResponseDTO<Category>("Tìm loại sản phẩm thành công", "success",
				category);
		return new ResponseEntity<ApiResponseDTO<Category>>(response, HttpStatus.OK);
	}

}

package com.example.demo.category.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.category.entity.Category;
import com.example.demo.category.service.CategoryService;
import com.example.demo.util.response.Pagination;
import com.example.demo.util.response.ResponseFactory;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	private CategoryService categoryService;
	private ResponseFactory responseFactory;

	public CategoryController(CategoryService categoryService, ResponseFactory responseFactory) {
		this.categoryService = categoryService;
		this.responseFactory = responseFactory;
	}

	@GetMapping
	public ResponseEntity<?> getAll(@RequestParam(required = false) String search,
			@RequestParam(defaultValue = "asc") String orderBy, 
			@RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int size) {
		Page<Category> categoriesPage = categoryService.findAll(search, orderBy, sortBy, page, size);
		Pagination pagination = new Pagination(categoriesPage.getNumber(), categoriesPage.getSize(),
				categoriesPage.getTotalElements(), categoriesPage.getTotalPages());
		return responseFactory.success(categoriesPage.getContent(),
				"Lấy danh sách loại sản phẩm thành công", pagination);
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<?> findById(@PathVariable String categoryId) {
		Category category = categoryService.findById(categoryId);
		return responseFactory.success(category, "Tìm kiếm thành công");
	}
}

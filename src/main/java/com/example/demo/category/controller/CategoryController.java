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

import com.example.demo.category.dto.CategoryResponseDTO;
import com.example.demo.category.dto.CategoryTreeResponseDTO;
import com.example.demo.category.entity.Category;
import com.example.demo.category.service.CategoryService;
import com.example.demo.util.base.dto.ApiResponseDTO;
import com.example.demo.util.base.dto.PagedResponseDTO;

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
		Page<Category> categories = categoryService.getAll(orderBy, sortBy, page, size);
		Page<CategoryResponseDTO> categoriesDTO = categories.map(CategoryResponseDTO::fromEntity);
		PagedResponseDTO<CategoryResponseDTO> pagedResponseDTO = PagedResponseDTO
				.convertPageToPagedResponseDTO(categoriesDTO);
		var response = new ApiResponseDTO<PagedResponseDTO<CategoryResponseDTO>>(
				"Lấy danh sách loại sản phẩm thành công", true, pagedResponseDTO);
		return new ResponseEntity<ApiResponseDTO<PagedResponseDTO<CategoryResponseDTO>>>(response, HttpStatus.OK);
	}

	@GetMapping("/assignable")
	public ResponseEntity<?> getLeafCategories() {
		List<Category> categories = categoryService.getLeafCategories();
		List<CategoryResponseDTO> categoriesDTO = categories.stream().map(CategoryResponseDTO::fromEntity).toList();
		var response = new ApiResponseDTO<List<CategoryResponseDTO>>(
				"Lấy danh sách loại sản phẩm có thể gán cho sản phẩm thành công", true, categoriesDTO);
		return new ResponseEntity<ApiResponseDTO<List<CategoryResponseDTO>>>(response, HttpStatus.OK);
	}

	@GetMapping("/tree")
	public ResponseEntity<?> getTree() {
		Category root = categoryService.getTree();
		CategoryTreeResponseDTO categoryTreeResponseDTO = CategoryTreeResponseDTO.fromEntity(root);
		var response = new ApiResponseDTO<CategoryTreeResponseDTO>("Lấy menu thành công", true,
				categoryTreeResponseDTO);
		return new ResponseEntity<ApiResponseDTO<CategoryTreeResponseDTO>>(response, HttpStatus.OK);
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<?> get(@PathVariable String categoryId) {
		Category category = categoryService.get(categoryId);
		CategoryResponseDTO categoryDTO = CategoryResponseDTO.fromEntity(category);
		var response = new ApiResponseDTO<CategoryResponseDTO>("Tìm loại sản phẩm thành công", true, categoryDTO);
		return new ResponseEntity<ApiResponseDTO<CategoryResponseDTO>>(response, HttpStatus.OK);
	}

	@GetMapping("{categoryId}/children")
	public ResponseEntity<?> getChildren(@PathVariable String categoryId,
			@RequestParam(defaultValue = "asc") String orderBy, @RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		Page<Category> categories = categoryService.getAll(orderBy, sortBy, page, size);
		Page<CategoryResponseDTO> categoriesDTO = categories.map(CategoryResponseDTO::fromEntity);
		PagedResponseDTO<CategoryResponseDTO> pagedResponseDTO = PagedResponseDTO
				.convertPageToPagedResponseDTO(categoriesDTO);
		var response = new ApiResponseDTO<PagedResponseDTO<CategoryResponseDTO>>(
				"Lấy danh sách loại sản phẩm con thành công", true, pagedResponseDTO);
		return new ResponseEntity<ApiResponseDTO<PagedResponseDTO<CategoryResponseDTO>>>(response, HttpStatus.OK);
	}
}

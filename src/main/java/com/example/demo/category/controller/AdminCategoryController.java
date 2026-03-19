package com.example.demo.category.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.demo.util.response.ResponseFactory;
import com.example.demo.util.response.Pagination;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

	private CategoryService categoryService;
	private ResponseFactory responseFactory;

	public AdminCategoryController(CategoryService categoryService, ResponseFactory responseFactory) {
		this.categoryService = categoryService;
		this.responseFactory = responseFactory;
	}

	@GetMapping
	public ResponseEntity<?> getAll(@RequestParam(required = false) String search,
			@RequestParam(defaultValue = "asc") String orderBy, @RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Page<Category> categories = categoryService.findAll(search, orderBy, sortBy, page, size);
		Pagination pagination = new Pagination(categories.getNumber(), categories.getSize(), categories.getTotalElements(),
				categories.getTotalPages());
		return responseFactory.success(categories.getContent(),
				"Lấy danh sách loại sản phẩm thành công", pagination);
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<?> findById(@PathVariable String categoryId) {
		Category category = categoryService.findById(categoryId);
		return responseFactory.success(category, "Tìm kiếm thành công");
	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody CreateCategoryRequestDTO body) {

		Category myCategory = categoryService.create(body);
		return responseFactory.success(myCategory, "Tạo loại sản phẩm thành công", HttpStatus.CREATED);
	}

	@PutMapping("/{categoryId}")
	public ResponseEntity<?> update(@PathVariable String categoryId,
			@Valid @RequestBody UpdateCategoryNameRequestDTO updateCategoryNameRequestDTO) {

		Category category = categoryService.update(updateCategoryNameRequestDTO, categoryId);

		return responseFactory.success(category, "Cập nhật loại sản phẩm thành công");
	}

	@DeleteMapping("/{categoryId}")
	public ResponseEntity<?> delete(@PathVariable String categoryId) {
		categoryService.delete(categoryId);
		return responseFactory.success("Xóa loại sản phẩm thành công");
	}

}
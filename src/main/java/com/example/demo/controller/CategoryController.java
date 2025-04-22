package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.example.demo.entities.Category;
import com.example.demo.services.interfaces.CategoryServiceInf;
import com.example.demo.validator.CreateCategoryValidator;
import com.example.demo.validator.UpdateCategoryValidator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	
	private CategoryServiceInf categoryService;

	public CategoryController(CategoryServiceInf categoryService) {
		this.categoryService = categoryService;
	}
	
	@GetMapping
	public ResponseEntity<?> getCategories() {
		List<Category> categories = categoryService.getCategories();
		ApiResponseDTO<List<Category>> response = new ApiResponseDTO<List<Category>>("Lấy thông tin loại sản phẩm thành công", categories);
		return new ResponseEntity<ApiResponseDTO<List<Category>>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/{categoryId}")
	public ResponseEntity<?> findCategoryById(@PathVariable String categoryId) {
		Category category = categoryService.findCategoryById(categoryId);
		ApiResponseDTO<Category> response = new ApiResponseDTO<Category>("Lấy thông tin loại sản phẩm thành công", category);
		return new ResponseEntity<ApiResponseDTO<Category>>(response, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<?> createCategory(@Valid @RequestBody CreateCategoryValidator body, BindingResult result) {
		
		if (result.hasErrors()) {
			Map<String, String> errors = new HashMap<String, String>();
			result.getFieldErrors().stream().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});
			ApiResponseDTO<Map<String, String>> response  = new ApiResponseDTO<>("Tạo mới loại sản phẩm thất bại", errors);
			return new ResponseEntity<ApiResponseDTO<Map<String, String>>>(response, HttpStatus.BAD_REQUEST);
		}
		
		Category myCategory = categoryService.createCategory(body);
		ApiResponseDTO<Category> response = new ApiResponseDTO<Category>("Tạo loại sản phẩm thành công", myCategory);
		return new ResponseEntity<ApiResponseDTO<Category>>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<?> deleteCategory(@PathVariable String categoryId) {
		categoryService.deleteCategory(categoryId);
		ApiResponseDTO<Void> response = new ApiResponseDTO<Void>(String.format("Đã xóa loại sản phẩm với id là: %s", categoryId));
		return new ResponseEntity<ApiResponseDTO<Void>>(response, HttpStatus.OK);
	}
	
	@PatchMapping
	public ResponseEntity<?> updateCategory(@RequestBody UpdateCategoryValidator body) {
		Category myCategory = categoryService.updateCategory(body);
		ApiResponseDTO<Category> response = new ApiResponseDTO<Category>("Cập nhật loại sản phẩm thành công", myCategory);
		return new ResponseEntity<ApiResponseDTO<Category>>(response, HttpStatus.OK);
	}
	
}

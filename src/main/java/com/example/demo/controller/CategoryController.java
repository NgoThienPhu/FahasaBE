package com.example.demo.controller;

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

import com.example.demo.dto.ApiResponseDTO;
import com.example.demo.dto.CreateCategoryRequestDTO;
import com.example.demo.dto.PagedResponseDTO;
import com.example.demo.dto.UpdateCategoryNameRequestDTO;
import com.example.demo.entities.Category;
import com.example.demo.services.interfaces.CategoryService;
import com.example.demo.utils.BindingResultUtils;

import jakarta.validation.Valid;

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

	@PostMapping
	public ResponseEntity<?> createCategoryRequestDTO(@Valid @RequestBody CreateCategoryRequestDTO body, BindingResult result) {

		ResponseEntity<?> responseError = BindingResultUtils.handleValidationErrors(result,
				"Tạo mới loại sản phẩm thất bại!");
		if (responseError != null)
			return responseError;

		Category myCategory = categoryService.create(body);
		ApiResponseDTO<Category> response = new ApiResponseDTO<Category>("Tạo loại sản phẩm thành công", "success",
				myCategory);
		return new ResponseEntity<ApiResponseDTO<Category>>(response, HttpStatus.OK);
	}

	@PatchMapping("/{categoryId}")
	public ResponseEntity<?> updateCategoryName(@PathVariable String categoryId,
			@Valid @RequestBody UpdateCategoryNameRequestDTO updateCategoryNameRequestDTO, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtils.handleValidationErrors(result,
				"Cập nhật loại sản phẩm thất bại!");
		if (responseError != null)
			return responseError;
		Category category = categoryService.update(updateCategoryNameRequestDTO, categoryId);
		ApiResponseDTO<Category> response = new ApiResponseDTO<Category>("Cập nhật loại sản phẩm thành công", "success",
				category);
		return new ResponseEntity<ApiResponseDTO<Category>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{categoryId}")
	public ResponseEntity<?> deleteCategory(@PathVariable String categoryId) {
		categoryService.deleteById(categoryId);
		ApiResponseDTO<Void> response = new ApiResponseDTO<Void>(
				String.format("Đã xóa loại sản phẩm với id là: %s", categoryId), "success");
		return new ResponseEntity<ApiResponseDTO<Void>>(response, HttpStatus.OK);
	}

}

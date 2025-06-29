package com.example.demo.controller;

import java.util.List;

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
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.ApiResponseDTO;
import com.example.demo.entities.Category;
import com.example.demo.entities.Product;
import com.example.demo.services.interfaces.CategoryServiceInf;
import com.example.demo.services.interfaces.ProductServiceInf;
import com.example.demo.utils.BindingResultUtils;
import com.example.demo.validator.CategoryValidator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	private CategoryServiceInf categoryService;
	private ProductServiceInf productService;

	public CategoryController(CategoryServiceInf categoryService, ProductServiceInf productService) {
		this.categoryService = categoryService;
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<?> getCategories(@RequestParam(required = false) String categoryName,
			@RequestParam(required = true, defaultValue = "asc") String orderBy) {
		List<Category> categories = categoryService.getCategories(categoryName, orderBy);
		ApiResponseDTO<List<Category>> response = new ApiResponseDTO<List<Category>>(
				"Lấy danh sách loại sản phẩm thành công", "success", categories);
		return new ResponseEntity<ApiResponseDTO<List<Category>>>(response, HttpStatus.OK);
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<?> findCategoryById(@PathVariable String categoryId) {
		Category category = categoryService.findCategoryById(categoryId);
		ApiResponseDTO<Category> response = new ApiResponseDTO<Category>("Tìm loại sản phẩm thành công", "success",
				category);
		return new ResponseEntity<ApiResponseDTO<Category>>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryValidator body, BindingResult result) {

		ResponseEntity<?> responseError = BindingResultUtils.handleValidationErrors(result,
				"Tạo mới loại sản phẩm thất bại!");
		if (responseError != null)
			return responseError;

		Category myCategory = categoryService.createCategory(body);
		ApiResponseDTO<Category> response = new ApiResponseDTO<Category>("Tạo loại sản phẩm thành công", "success",
				myCategory);
		return new ResponseEntity<ApiResponseDTO<Category>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{categoryId}")
	public ResponseEntity<?> deleteCategory(@PathVariable String categoryId) {
		List<Product> products = productService.findProductsByCateogryId(categoryId);
		if (!products.isEmpty())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Loại sản phẩm đang được sử dụng vui lòng thử lại sau");
		categoryService.deleteCategory(categoryId);
		ApiResponseDTO<Void> response = new ApiResponseDTO<Void>(
				String.format("Đã xóa loại sản phẩm với id là: %s", categoryId), "success");
		return new ResponseEntity<ApiResponseDTO<Void>>(response, HttpStatus.OK);
	}

	@PatchMapping("/{categoryId}")
	public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryValidator body, @PathVariable String categoryId,
			BindingResult result) {

		ResponseEntity<?> responseError = BindingResultUtils.handleValidationErrors(result,
				"Cập nhật loại sản phẩm thất bại!");
		if (responseError != null)
			return responseError;

		Category myCategory = categoryService.updateCategory(body, categoryId);
		ApiResponseDTO<Category> response = new ApiResponseDTO<Category>("Cập nhật loại sản phẩm thành công", "success",
				myCategory);
		return new ResponseEntity<ApiResponseDTO<Category>>(response, HttpStatus.OK);
	}

}

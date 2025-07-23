package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.ApiResponseDTO;
import com.example.demo.dto.CreateProductRequestDTO;
import com.example.demo.dto.PagedResponseDTO;
import com.example.demo.dto.ProductFilterDTO;
import com.example.demo.dto.UpdateProductRequestDTO;
import com.example.demo.entities.Product;
import com.example.demo.services.interfaces.ProductService;
import com.example.demo.util.validation.BindingResultUtil;
import com.example.demo.util.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	@JsonView(View.Public.class)
	public ResponseEntity<?> getProducts(@RequestBody(required = false) ProductFilterDTO dto,
			@RequestParam(required = true, defaultValue = "name") String sortBy,
			@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<Product> products = productService.findAll(dto, orderBy, sortBy, page, size);
		PagedResponseDTO<Product> pagedResponseDTO = PagedResponseDTO.convertPageToPagedResponseDTO(products);
		ApiResponseDTO<PagedResponseDTO<Product>> response = new ApiResponseDTO<PagedResponseDTO<Product>>(
				"Tìm sản phẩm thành công", "success", pagedResponseDTO);
		return new ResponseEntity<ApiResponseDTO<PagedResponseDTO<Product>>>(response, HttpStatus.OK);
	}

	@GetMapping("/{productId}")
	@JsonView(View.Public.class)
	public ResponseEntity<?> getProductById(@PathVariable String productId) {
		Product product = productService.findById(productId);
		if (product == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Không tìm thấy sản phẩm với id là: %s", productId));
		ApiResponseDTO<Product> response = new ApiResponseDTO<Product>("Tìm sản phẩm thành công", "success", product);
		return new ResponseEntity<ApiResponseDTO<Product>>(response, HttpStatus.OK);
	}

	@PostMapping
	@JsonView(View.Admin.class)
	public ResponseEntity<?> createProduct(@RequestPart(required = false) List<MultipartFile> images,
			@RequestPart(required = true) MultipartFile image,
			@RequestPart(required = true) @Valid CreateProductRequestDTO product, BindingResult result)
			throws IOException {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result,
				"Tạo mới sản phẩm thất bại!");
		if (responseError != null)
			return responseError;

		Product myProduct = (images != null) ? productService.createProduct(product, image, images)
				: productService.createProduct(product, image);

		ApiResponseDTO<Product> response = new ApiResponseDTO<Product>("Tạo sản phẩm thành công", "success", myProduct);
		return new ResponseEntity<ApiResponseDTO<Product>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{productId}")
	@JsonView(View.Public.class)
	public ResponseEntity<?> deleteProductById(@PathVariable String productId) {
		productService.deleteById(productId);
		ApiResponseDTO<Void> response = new ApiResponseDTO<Void>("Xóa sản phẩm thành công", "success");
		return new ResponseEntity<ApiResponseDTO<Void>>(response, HttpStatus.OK);
	}

	@PatchMapping("/{productId}")
	@JsonView(View.Admin.class)
	public ResponseEntity<?> updateProductById(@PathVariable String productId,
			@RequestBody UpdateProductRequestDTO dto) {
		Product product = productService.updateProduct(productId, dto);
		ApiResponseDTO<Product> response = new ApiResponseDTO<Product>("Cập nhật sản phẩm thành công", "success",
				product);
		return new ResponseEntity<ApiResponseDTO<Product>>(response, HttpStatus.OK);
	}

	@PatchMapping("/{productId}/main-image")
	@JsonView(View.Admin.class)
	public ResponseEntity<?> updateMainImage(@PathVariable String productId,
			@RequestPart(required = true) MultipartFile image) throws Exception {
		Product product = productService.updateNewMainImage(productId, image);
		ApiResponseDTO<Product> response = new ApiResponseDTO<Product>("Cập nhật ảnh chính của sản phẩm thành công",
				"success", product);
		return new ResponseEntity<ApiResponseDTO<Product>>(response, HttpStatus.OK);
	}

	@PatchMapping("/{productId}/images")
	@JsonView(View.Admin.class)
	public ResponseEntity<?> updateImages(@PathVariable String productId,
			@RequestPart(required = true) List<MultipartFile> images) {
		Product product = productService.updateImages(productId, images);
		ApiResponseDTO<Product> response = new ApiResponseDTO<Product>("Cập nhật danh sách ảnh của sản phẩm thành công",
				"success", product);
		return new ResponseEntity<ApiResponseDTO<Product>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{productId}/images")
	@JsonView(View.Admin.class)
	public ResponseEntity<?> deleteImages(@PathVariable String productId,
			@RequestBody(required = true) List<String> imagesId) {
		Product product = productService.deleteImages(productId, imagesId);
		ApiResponseDTO<Product> response = new ApiResponseDTO<Product>("Xóa danh sách ảnh của sản phẩm thành công",
				"success", product);
		return new ResponseEntity<ApiResponseDTO<Product>>(response, HttpStatus.OK);
	}

}

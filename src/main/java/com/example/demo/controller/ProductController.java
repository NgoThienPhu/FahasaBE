package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.ApiResponseDTO;
import com.example.demo.dto.ProductRequestDTO;
import com.example.demo.entities.Product;
import com.example.demo.services.interfaces.ProductService;
import com.example.demo.utils.BindingResultUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
//	@GetMapping
//	public ResponseEntity<?> getProducts() {
//		
//	}

	@GetMapping("/{productId}")
	public ResponseEntity<?> getProductById(@PathVariable String productId) {
		Product product = productService.findById(productId);
		if (product == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Không tìm thấy sản phẩm với id là: %s", productId));
		ApiResponseDTO<Product> response = new ApiResponseDTO<Product>("Tìm sản phẩm thành công", "success", product);
		return new ResponseEntity<ApiResponseDTO<Product>>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> createProduct(@RequestPart List<MultipartFile> images, @RequestPart MultipartFile image,
			@RequestPart @Valid ProductRequestDTO product, BindingResult result) throws IOException {
		ResponseEntity<?> responseError = BindingResultUtils.handleValidationErrors(result,
				"Tạo mới sản phẩm thất bại!");
		if (responseError != null)
			return responseError;

		Product myProduct = productService.createProduct(product, image, images);
		ApiResponseDTO<Product> response = new ApiResponseDTO<Product>("Tạo sản phẩm thành công", "success", myProduct);
		return new ResponseEntity<ApiResponseDTO<Product>>(response, HttpStatus.OK);
	}

}

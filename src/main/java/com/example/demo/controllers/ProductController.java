package com.example.demo.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.common.ApiResponseDTO;
import com.example.demo.dto.common.PagedResponseDTO;
import com.example.demo.dto.product.ProductFilterDTO;
import com.example.demo.dto.product.ProductResponseDTO;
import com.example.demo.entities.Product;
import com.example.demo.services.interfaces.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
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
	public ResponseEntity<?> getProductById(@PathVariable String productId) {
		ProductResponseDTO product = productService.findById(productId);
		if (product == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Không tìm thấy sản phẩm với id là: %s", productId));
		ApiResponseDTO<ProductResponseDTO> response = new ApiResponseDTO<>("Tìm sản phẩm thành công", "success", product);
		return new ResponseEntity<ApiResponseDTO<ProductResponseDTO>>(response, HttpStatus.OK);
	}

}

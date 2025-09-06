package com.example.demo.product.controller;

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

import com.example.demo.common.base.dto.ApiResponseDTO;
import com.example.demo.common.base.dto.PagedResponseDTO;
import com.example.demo.common.validation.BindingResultUtil;
import com.example.demo.product.application.ProductApplicationService;
import com.example.demo.product.dto.CreateProductRequestDTO;
import com.example.demo.product.dto.ProductResponseDTO;
import com.example.demo.product.dto.UpdateProductRequestDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

	private ProductApplicationService productApplicationService;

	public AdminProductController(ProductApplicationService productApplicationService) {
		this.productApplicationService = productApplicationService;
	}

	@GetMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getProducts(@RequestParam(required = true, defaultValue = "name") String sortBy,
			@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<ProductResponseDTO> products = productApplicationService.getPageProductResponseDTO(orderBy, sortBy, page,
				size);
		PagedResponseDTO<ProductResponseDTO> pagedResponseDTO = PagedResponseDTO
				.convertPageToPagedResponseDTO(products);
		var response = new ApiResponseDTO<PagedResponseDTO<ProductResponseDTO>>("Tìm sản phẩm thành công", true,
				pagedResponseDTO);
		return new ResponseEntity<ApiResponseDTO<PagedResponseDTO<ProductResponseDTO>>>(response, HttpStatus.OK);
	}

	@GetMapping("/{productId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getProductById(@PathVariable String productId) {
		ProductResponseDTO product = productApplicationService.getProductResponseDTOById(productId);
		if (product == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Không tìm thấy sản phẩm với id là: %s", productId));
		var response = new ApiResponseDTO<>("Tìm sản phẩm thành công", true, product);
		return new ResponseEntity<ApiResponseDTO<ProductResponseDTO>>(response, HttpStatus.OK);
	}

	@PostMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createProduct(@RequestPart(required = false) List<MultipartFile> images,
			@RequestPart(required = true) MultipartFile image,
			@RequestPart(required = true) @Valid CreateProductRequestDTO product, BindingResult result)
			throws IOException {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result,
				"Tạo mới sản phẩm thất bại!");
		if (responseError != null)
			return responseError;

		ProductResponseDTO myProduct = productApplicationService.create(product, image, images);

		var response = new ApiResponseDTO<>("Tạo sản phẩm thành công", true, myProduct);
		return new ResponseEntity<ApiResponseDTO<ProductResponseDTO>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{productId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteProductById(@PathVariable String productId) {
		productApplicationService.deleteById(productId);
		var response = new ApiResponseDTO<Void>("Xóa sản phẩm thành công", true);
		return new ResponseEntity<ApiResponseDTO<Void>>(response, HttpStatus.OK);
	}

	@PatchMapping("/{productId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateProductById(@PathVariable String productId,
			@RequestBody UpdateProductRequestDTO dto) {
		ProductResponseDTO product = productApplicationService.update(productId, dto);
		var response = new ApiResponseDTO<ProductResponseDTO>("Cập nhật sản phẩm thành công", true, product);
		return new ResponseEntity<ApiResponseDTO<ProductResponseDTO>>(response, HttpStatus.OK);
	}

}

package com.example.demo.productimage.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.common.base.dto.ApiResponseDTO;
import com.example.demo.product.application.ProductApplicationService;
import com.example.demo.product.dto.ProductResponseDTO;

@RestController
@RequestMapping("/api/admin/products/{productId}/images")
public class AdminProductImageController {

	private ProductApplicationService productApplicationService;

	public AdminProductImageController(ProductApplicationService productApplicationService) {
		this.productApplicationService = productApplicationService;
	}

	@PatchMapping("/main-image")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updatePrimaryImage(@PathVariable String productId,
			@RequestPart(required = true) MultipartFile image) throws Exception {
		ProductResponseDTO product = productApplicationService.updatePrimaryImage(productId, image);
		var response = new ApiResponseDTO<ProductResponseDTO>("Cập nhật ảnh chính của sản phẩm thành công", true,
				product);
		return new ResponseEntity<ApiResponseDTO<ProductResponseDTO>>(response, HttpStatus.OK);
	}

	@PatchMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateImages(@PathVariable String productId,
			@RequestPart(required = true) List<MultipartFile> images) {
		ProductResponseDTO product = productApplicationService.updateSecondImage(productId, images);
		var response = new ApiResponseDTO<ProductResponseDTO>("Cập nhật danh sách ảnh của sản phẩm thành công",
				true, product);
		return new ResponseEntity<ApiResponseDTO<ProductResponseDTO>>(response, HttpStatus.OK);
	}

	@DeleteMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteImages(@PathVariable String productId,
			@RequestBody(required = true) List<String> imagesId) {
		ProductResponseDTO product = productApplicationService.deleteSecondImage(productId, imagesId);
		var response = new ApiResponseDTO<ProductResponseDTO>("Xóa danh sách ảnh của sản phẩm thành công", true,
				product);
		return new ResponseEntity<ApiResponseDTO<ProductResponseDTO>>(response, HttpStatus.OK);
	}

}

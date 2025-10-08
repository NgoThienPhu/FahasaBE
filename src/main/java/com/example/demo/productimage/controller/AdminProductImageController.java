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

import com.example.demo.product.dto.ProductDetailDTO;
import com.example.demo.product.entity.Product;
import com.example.demo.product.mapper.ProductDetailMapper;
import com.example.demo.product.service.ProductService;
import com.example.demo.util.base.dto.ApiResponseDTO;

@RestController
@RequestMapping("/api/admin/products/{productId}/images")
public class AdminProductImageController {

	private ProductService productService;

	private ProductDetailMapper productDetailMapper;

	public AdminProductImageController(ProductService productService, ProductDetailMapper productDetailMapper) {
		this.productService = productService;
		this.productDetailMapper = productDetailMapper;
	}

	@PatchMapping("/main-image")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updatePrimaryImage(@PathVariable String productId,
			@RequestPart(required = true) MultipartFile image) throws Exception {
		Product product = productService.updatePrimaryImage(productId, image);
		ProductDetailDTO productDetail = productDetailMapper.convertToProductDetailDTO(product);
		var response = new ApiResponseDTO<ProductDetailDTO>("Cập nhật ảnh chính của sản phẩm thành công", true,
				productDetail);
		return new ResponseEntity<ApiResponseDTO<ProductDetailDTO>>(response, HttpStatus.OK);
	}

	@PatchMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateImages(@PathVariable String productId,
			@RequestPart(required = true) List<MultipartFile> images) {
		Product product = productService.updateSecondImage(productId, images);
		ProductDetailDTO productDetail = productDetailMapper.convertToProductDetailDTO(product);
		var response = new ApiResponseDTO<ProductDetailDTO>("Cập nhật danh sách ảnh của sản phẩm thành công", true,
				productDetail);
		return new ResponseEntity<ApiResponseDTO<ProductDetailDTO>>(response, HttpStatus.OK);
	}

	@DeleteMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteImages(@PathVariable String productId,
			@RequestBody(required = true) List<String> imagesId) {
		Product product = productService.deleteSecondImage(productId, imagesId);
		ProductDetailDTO productDetail = productDetailMapper.convertToProductDetailDTO(product);
		var response = new ApiResponseDTO<ProductDetailDTO>("Xóa danh sách ảnh của sản phẩm thành công", true,
				productDetail);
		return new ResponseEntity<ApiResponseDTO<ProductDetailDTO>>(response, HttpStatus.OK);
	}

}

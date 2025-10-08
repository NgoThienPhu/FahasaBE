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

import com.example.demo.product.dto.CreateProductRequestDTO;
import com.example.demo.product.dto.ProductDetailDTO;
import com.example.demo.product.dto.UpdateProductRequestDTO;
import com.example.demo.product.entity.Product;
import com.example.demo.product.flow.CreateProductFlow;
import com.example.demo.product.mapper.ProductDetailMapper;
import com.example.demo.product.service.ProductService;
import com.example.demo.util.base.dto.ApiResponseDTO;
import com.example.demo.util.base.dto.PagedResponseDTO;
import com.example.demo.util.validation.BindingResultUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

	private ProductService productService;
	
	private CreateProductFlow createProductFlow;

	private ProductDetailMapper productDetailMapper;

	public AdminProductController(ProductService productService, CreateProductFlow createProductFlow,
			ProductDetailMapper productDetailMapper) {
		this.productService = productService;
		this.createProductFlow = createProductFlow;
		this.productDetailMapper = productDetailMapper;
	}

	@GetMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getProducts(@RequestParam(required = true, defaultValue = "name") String sortBy,
			@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<Product> products = productService.findAll(orderBy, sortBy, page, size);
		Page<ProductDetailDTO> productDetails = products.map(productDetailMapper::convertToProductDetailDTO);
		PagedResponseDTO<ProductDetailDTO> pagedResponseDTO = PagedResponseDTO
				.convertPageToPagedResponseDTO(productDetails);
		var response = new ApiResponseDTO<PagedResponseDTO<ProductDetailDTO>>("Tìm sản phẩm thành công", true,
				pagedResponseDTO);
		return new ResponseEntity<ApiResponseDTO<PagedResponseDTO<ProductDetailDTO>>>(response, HttpStatus.OK);
	}

	@GetMapping("/{productId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getProductById(@PathVariable String productId) {
		Product product = productService.findById(productId);
		if (product == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Không tìm thấy sản phẩm với id là: %s", productId));
		ProductDetailDTO productDetail = productDetailMapper.convertToProductDetailDTO(product);
		var response = new ApiResponseDTO<>("Tìm sản phẩm thành công", true, productDetail);
		return new ResponseEntity<ApiResponseDTO<ProductDetailDTO>>(response, HttpStatus.OK);
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

		Product myProduct = createProductFlow.create(product, image, images);
		ProductDetailDTO productDetail = productDetailMapper.convertToProductDetailDTO(myProduct);

		var response = new ApiResponseDTO<>("Tạo sản phẩm thành công", true, productDetail);
		return new ResponseEntity<ApiResponseDTO<ProductDetailDTO>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{productId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteProductById(@PathVariable String productId) {
		productService.deleteById(productId);
		var response = new ApiResponseDTO<Void>("Xóa sản phẩm thành công", true);
		return new ResponseEntity<ApiResponseDTO<Void>>(response, HttpStatus.OK);
	}

	@PatchMapping("/{productId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateProductById(@PathVariable String productId,
			@RequestBody UpdateProductRequestDTO dto) {
		Product product = productService.update(productId, dto);
		ProductDetailDTO productDetail = productDetailMapper.convertToProductDetailDTO(product);
		var response = new ApiResponseDTO<ProductDetailDTO>("Cập nhật sản phẩm thành công", true, productDetail);
		return new ResponseEntity<ApiResponseDTO<ProductDetailDTO>>(response, HttpStatus.OK);
	}

}

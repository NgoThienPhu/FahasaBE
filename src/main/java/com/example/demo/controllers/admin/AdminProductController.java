package com.example.demo.controllers.admin;

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

import com.example.demo.dto.common.ApiResponseDTO;
import com.example.demo.dto.common.PagedResponseDTO;
import com.example.demo.dto.product.CreateProductRequestDTO;
import com.example.demo.dto.product.ProductFilterDTO;
import com.example.demo.dto.product.ProductResponseDTO;
import com.example.demo.dto.product.UpdateProductRequestDTO;
import com.example.demo.entities.Product;
import com.example.demo.services.interfaces.ProductService;
import com.example.demo.utils.validation.BindingResultUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

	private ProductService productService;

	public AdminProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
//	@PreAuthorize("hasRole('ADMIN')")
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
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getProductById(@PathVariable String productId) {
		ProductResponseDTO product = productService.findById(productId);
		if (product == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Không tìm thấy sản phẩm với id là: %s", productId));
		ApiResponseDTO<ProductResponseDTO> response = new ApiResponseDTO<>("Tìm sản phẩm thành công", "success", product);
		return new ResponseEntity<ApiResponseDTO<ProductResponseDTO>>(response, HttpStatus.OK);
	}

	@PostMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createProduct(@RequestPart(required = false) List<MultipartFile> images,
			@RequestPart(required = false) MultipartFile image,
			@RequestPart(required = true) @Valid CreateProductRequestDTO product, BindingResult result)
			throws IOException {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result,
				"Tạo mới sản phẩm thất bại!");
		if (responseError != null)
			return responseError;

		ProductResponseDTO myProduct = productService.createProduct(product, image, images);

		ApiResponseDTO<ProductResponseDTO> response = new ApiResponseDTO<>("Tạo sản phẩm thành công", "success", myProduct);
		return new ResponseEntity<ApiResponseDTO<ProductResponseDTO>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{productId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteProductById(@PathVariable String productId) {
		productService.deleteById(productId);
		ApiResponseDTO<Void> response = new ApiResponseDTO<Void>("Xóa sản phẩm thành công", "success");
		return new ResponseEntity<ApiResponseDTO<Void>>(response, HttpStatus.OK);
	}

	@PatchMapping("/{productId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateProductById(@PathVariable String productId,
			@RequestBody UpdateProductRequestDTO dto) {
		Product product = productService.updateProduct(productId, dto);
		ApiResponseDTO<Product> response = new ApiResponseDTO<Product>("Cập nhật sản phẩm thành công", "success",
				product);
		return new ResponseEntity<ApiResponseDTO<Product>>(response, HttpStatus.OK);
	}

	@PatchMapping("/{productId}/main-image")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateMainImage(@PathVariable String productId,
			@RequestPart(required = true) MultipartFile image) throws Exception {
		Product product = productService.updateNewMainImage(productId, image);
		ApiResponseDTO<Product> response = new ApiResponseDTO<Product>("Cập nhật ảnh chính của sản phẩm thành công",
				"success", product);
		return new ResponseEntity<ApiResponseDTO<Product>>(response, HttpStatus.OK);
	}

	@PatchMapping("/{productId}/images")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateImages(@PathVariable String productId,
			@RequestPart(required = true) List<MultipartFile> images) {
		Product product = productService.updateImages(productId, images);
		ApiResponseDTO<Product> response = new ApiResponseDTO<Product>("Cập nhật danh sách ảnh của sản phẩm thành công",
				"success", product);
		return new ResponseEntity<ApiResponseDTO<Product>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{productId}/images")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteImages(@PathVariable String productId,
			@RequestBody(required = true) List<String> imagesId) {
		Product product = productService.deleteImages(productId, imagesId);
		ApiResponseDTO<Product> response = new ApiResponseDTO<Product>("Xóa danh sách ảnh của sản phẩm thành công",
				"success", product);
		return new ResponseEntity<ApiResponseDTO<Product>>(response, HttpStatus.OK);
	}

}

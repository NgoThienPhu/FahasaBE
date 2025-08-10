package com.example.demo.controllers.admin;

import java.io.IOException;
import java.math.BigDecimal;
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
import com.example.demo.dto.price.CreatePromoPriceRequestDTO;
import com.example.demo.dto.price.PromoPriceResponseDTO;
import com.example.demo.dto.product.CreateProductRequestDTO;
import com.example.demo.dto.product.ProductFilterDTO;
import com.example.demo.dto.product.ProductResponseDTO;
import com.example.demo.dto.product.UpdateProductRequestDTO;
import com.example.demo.entities.Product;
import com.example.demo.entities.price.PromoPrice;
import com.example.demo.entities.price.PurchasePrice;
import com.example.demo.entities.price.SellPrice;
import com.example.demo.services.interfaces.ProductImageService;
import com.example.demo.services.interfaces.ProductPriceService;
import com.example.demo.services.interfaces.ProductService;
import com.example.demo.utils.validation.BindingResultUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

	private ProductService productService;

	private ProductPriceService productPriceService;

	private ProductImageService productImageService;

	public AdminProductController(ProductService productService, ProductPriceService productPriceService,
			ProductImageService productImageService) {
		this.productService = productService;
		this.productPriceService = productPriceService;
		this.productImageService = productImageService;
	}

	@GetMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getProducts(@RequestBody(required = false) ProductFilterDTO dto,
			@RequestParam(required = true, defaultValue = "name") String sortBy,
			@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<ProductResponseDTO> products = productService.findAll(dto, orderBy, sortBy, page, size);
		PagedResponseDTO<ProductResponseDTO> pagedResponseDTO = PagedResponseDTO
				.convertPageToPagedResponseDTO(products);
		ApiResponseDTO<PagedResponseDTO<ProductResponseDTO>> response = new ApiResponseDTO<PagedResponseDTO<ProductResponseDTO>>(
				"Tìm sản phẩm thành công", "success", pagedResponseDTO);
		return new ResponseEntity<ApiResponseDTO<PagedResponseDTO<ProductResponseDTO>>>(response, HttpStatus.OK);
	}

	@GetMapping("/{productId}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getProductById(@PathVariable String productId) {
		ProductResponseDTO product = productService.getProductResponseById(productId);
		if (product == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Không tìm thấy sản phẩm với id là: %s", productId));
		ApiResponseDTO<ProductResponseDTO> response = new ApiResponseDTO<>("Tìm sản phẩm thành công", "success",
				product);
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

		ApiResponseDTO<ProductResponseDTO> response = new ApiResponseDTO<>("Tạo sản phẩm thành công", "success",
				myProduct);
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
		productImageService.updateProductMainImage(productId, image);
		Product product = productService.getProductEntityById(productId);
		ApiResponseDTO<Product> response = new ApiResponseDTO<Product>("Cập nhật ảnh chính của sản phẩm thành công",
				"success", product);
		return new ResponseEntity<ApiResponseDTO<Product>>(response, HttpStatus.OK);
	}

	@PatchMapping("/{productId}/images")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateImages(@PathVariable String productId,
			@RequestPart(required = true) List<MultipartFile> images) {
		productImageService.updateProductImages(productId, images);
		Product product = productService.getProductEntityById(productId);
		ApiResponseDTO<Product> response = new ApiResponseDTO<Product>("Cập nhật danh sách ảnh của sản phẩm thành công",
				"success", product);
		return new ResponseEntity<ApiResponseDTO<Product>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{productId}/images")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteImages(@PathVariable String productId,
			@RequestBody(required = true) List<String> imagesId) {
		productImageService.deleteProductImages(productId, imagesId);
		Product product = productService.getProductEntityById(productId);
		ApiResponseDTO<Product> response = new ApiResponseDTO<Product>("Xóa danh sách ảnh của sản phẩm thành công",
				"success", product);
		return new ResponseEntity<ApiResponseDTO<Product>>(response, HttpStatus.OK);
	}

	@GetMapping("/{productId}/sell-prices")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllSellPrice(@PathVariable(required = false) String productId,
			@RequestParam(required = true, defaultValue = "startDate") String sortBy,
			@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<SellPrice> sellPrices = productPriceService.getAllSellPriceByProduct(productId, sortBy, orderBy, page,
				size);
		PagedResponseDTO<SellPrice> pagedResponseDTO = PagedResponseDTO.convertPageToPagedResponseDTO(sellPrices);
		ApiResponseDTO<PagedResponseDTO<SellPrice>> response = new ApiResponseDTO<PagedResponseDTO<SellPrice>>(
				"Lấy danh sách giá bán của sản phẩm thành công", "success", pagedResponseDTO);
		return new ResponseEntity<ApiResponseDTO<PagedResponseDTO<SellPrice>>>(response, HttpStatus.OK);
	}

	@PostMapping("/{productId}/sell-prices")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createSellPrice(@PathVariable(required = true) String productId,
			@RequestParam(required = true) BigDecimal newSellPrice) {
		SellPrice sellPrice = productPriceService.updateProductSellPrice(productId, newSellPrice);
		ApiResponseDTO<SellPrice> response = new ApiResponseDTO<>("Tạo giá bán của sản phẩm thành công", "success",
				sellPrice);
		return new ResponseEntity<ApiResponseDTO<SellPrice>>(response, HttpStatus.OK);
	}

	@GetMapping("/{productId}/purchase-prices")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllPurchasePrice(@PathVariable(required = false) String productId,
			@RequestParam(required = true, defaultValue = "startDate") String sortBy,
			@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<PurchasePrice> purchasePrices = productPriceService.getAllPurchasePriceByProduct(productId, sortBy,
				orderBy, page, size);
		PagedResponseDTO<PurchasePrice> pagedResponseDTO = PagedResponseDTO
				.convertPageToPagedResponseDTO(purchasePrices);
		ApiResponseDTO<PagedResponseDTO<PurchasePrice>> response = new ApiResponseDTO<PagedResponseDTO<PurchasePrice>>(
				"Lấy danh sách giá nhập của sản phẩm thành công", "success", pagedResponseDTO);
		return new ResponseEntity<ApiResponseDTO<PagedResponseDTO<PurchasePrice>>>(response, HttpStatus.OK);
	}

	@PostMapping("/{productId}/purchase-prices")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createPurchasePrice(@PathVariable(required = true) String productId,
			@RequestParam(required = true) BigDecimal newPurchasePrice) {
		PurchasePrice purchasePrice = productPriceService.updateProductPurchasePrice(productId, newPurchasePrice);
		ApiResponseDTO<PurchasePrice> response = new ApiResponseDTO<>("Tạo giá nhập của sản phẩm thành công", "success",
				purchasePrice);
		return new ResponseEntity<ApiResponseDTO<PurchasePrice>>(response, HttpStatus.OK);
	}

	@GetMapping("/{productId}/promo-prices")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllPromoPrice(@PathVariable(required = false) String productId,
			@RequestParam(required = true, defaultValue = "startDate") String sortBy,
			@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<PromoPrice> promoPrices = productPriceService.getAllPromoPriceByProduct(productId, sortBy, orderBy, page,
				size);
		Page<PromoPriceResponseDTO> promoPricesDTO = promoPrices.map(PromoPriceResponseDTO::fromEntity);
		PagedResponseDTO<PromoPriceResponseDTO> pagedResponseDTO = PagedResponseDTO
				.convertPageToPagedResponseDTO(promoPricesDTO);
		ApiResponseDTO<PagedResponseDTO<PromoPriceResponseDTO>> response = new ApiResponseDTO<PagedResponseDTO<PromoPriceResponseDTO>>(
				"Lấy danh sách giá khuyến mại của sản phẩm thành công", "success", pagedResponseDTO);
		return new ResponseEntity<ApiResponseDTO<PagedResponseDTO<PromoPriceResponseDTO>>>(response, HttpStatus.OK);
	}

	@PostMapping("/{productId}/promo-prices")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createPromoPrice(@PathVariable String productId,
			@RequestBody CreatePromoPriceRequestDTO dto) {
		PromoPrice promoPrice = productPriceService.createProductPromoPrice(productId, dto);
		ApiResponseDTO<PromoPrice> response = new ApiResponseDTO<>("Tạo giá khuyến mại của sản phẩm thành công",
				"success", promoPrice);
		return new ResponseEntity<ApiResponseDTO<PromoPrice>>(response, HttpStatus.OK);
	}

}

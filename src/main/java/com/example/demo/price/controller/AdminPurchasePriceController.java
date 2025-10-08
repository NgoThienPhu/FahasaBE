package com.example.demo.price.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.price.entity.PurchasePrice;
import com.example.demo.price.service.PurchasePriceService;
import com.example.demo.util.base.dto.ApiResponseDTO;
import com.example.demo.util.base.dto.PagedResponseDTO;

@RestController
@RequestMapping("/api/admin/products/{productId}/purchase-prices")
public class AdminPurchasePriceController {

	private PurchasePriceService purchasePriceService;

	public AdminPurchasePriceController(PurchasePriceService purchasePriceService) {
		this.purchasePriceService = purchasePriceService;
	}

	@GetMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllPurchasePrice(@PathVariable(required = true) String productId,
			@RequestParam(required = true, defaultValue = "startDate") String sortBy,
			@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<PurchasePrice> purchasePrices = purchasePriceService.findAll(productId, sortBy, orderBy, page, size);
		PagedResponseDTO<PurchasePrice> pagedResponseDTO = PagedResponseDTO
				.convertPageToPagedResponseDTO(purchasePrices);
		var response = new ApiResponseDTO<PagedResponseDTO<PurchasePrice>>(
				"Lấy danh sách giá nhập của sản phẩm thành công", true, pagedResponseDTO);
		return new ResponseEntity<ApiResponseDTO<PagedResponseDTO<PurchasePrice>>>(response, HttpStatus.OK);
	}

	@PostMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createPurchasePrice(@PathVariable(required = true) String productId,
			@RequestParam(required = true) BigDecimal newPurchasePrice) {
		PurchasePrice purchasePrice = purchasePriceService.update(productId, newPurchasePrice);
		var response = new ApiResponseDTO<>("Tạo giá nhập của sản phẩm thành công", true, purchasePrice);
		return new ResponseEntity<ApiResponseDTO<PurchasePrice>>(response, HttpStatus.OK);
	}

}

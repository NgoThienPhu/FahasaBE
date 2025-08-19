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

import com.example.demo.common.base.dto.ApiResponseDTO;
import com.example.demo.common.base.dto.PagedResponseDTO;
import com.example.demo.price.entity.SellPrice;
import com.example.demo.product.application.ProductApplicationService;

@RestController
@RequestMapping("/api/admin/products/{productId}/sell-prices")
public class AdminSellPriceController {
	
	private ProductApplicationService productApplicationService;
	
	public AdminSellPriceController(ProductApplicationService productApplicationService) {
		this.productApplicationService = productApplicationService;
	}

	@GetMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllSellPrice(@PathVariable(required = true) String productId,
			@RequestParam(required = true, defaultValue = "startDate") String sortBy,
			@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<SellPrice> sellPrices = productApplicationService.findAllSellPrice(productId, sortBy, orderBy, page, size);
		PagedResponseDTO<SellPrice> pagedResponseDTO = PagedResponseDTO.convertPageToPagedResponseDTO(sellPrices);
		ApiResponseDTO<PagedResponseDTO<SellPrice>> response = new ApiResponseDTO<PagedResponseDTO<SellPrice>>(
				"Lấy danh sách giá bán của sản phẩm thành công", "success", pagedResponseDTO);
		return new ResponseEntity<ApiResponseDTO<PagedResponseDTO<SellPrice>>>(response, HttpStatus.OK);
	}

	@PostMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createSellPrice(@PathVariable(required = true) String productId,
			@RequestParam(required = true) BigDecimal newSellPrice) {
		SellPrice sellPrice = productApplicationService.createSellPrice(productId, newSellPrice);
		ApiResponseDTO<SellPrice> response = new ApiResponseDTO<>("Tạo giá bán của sản phẩm thành công", "success",
				sellPrice);
		return new ResponseEntity<ApiResponseDTO<SellPrice>>(response, HttpStatus.OK);
	}
	
}

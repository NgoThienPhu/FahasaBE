package com.example.demo.price.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.base.dto.ApiResponseDTO;
import com.example.demo.common.base.dto.PagedResponseDTO;
import com.example.demo.price.dto.CreatePromoPriceRequestDTO;
import com.example.demo.price.dto.PromoPriceResponseDTO;
import com.example.demo.price.entity.PromoPrice;
import com.example.demo.product.application.ProductApplicationService;
import com.example.demo.product.entity.Product;

@RestController
@RequestMapping("/api/admin/products/{productId}/promo-prices")
public class AdminPromoPriceController {

	private ProductApplicationService productApplicationService;

	public AdminPromoPriceController(ProductApplicationService productApplicationService) {
		this.productApplicationService = productApplicationService;
	}

	@GetMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllPromoPrice(@PathVariable(required = false) String productId,
			@RequestParam(required = true, defaultValue = "startDate") String sortBy,
			@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<PromoPrice> promoPrices = productApplicationService.findAllPromoPrice(productId, sortBy, orderBy, page,
				size);
		Page<PromoPriceResponseDTO> promoPricesDTO = promoPrices.map(PromoPriceResponseDTO::fromEntity);
		PagedResponseDTO<PromoPriceResponseDTO> pagedResponseDTO = PagedResponseDTO
				.convertPageToPagedResponseDTO(promoPricesDTO);
		var response = new ApiResponseDTO<PagedResponseDTO<PromoPriceResponseDTO>>(
				"Lấy danh sách giá khuyến mại của sản phẩm thành công", true, pagedResponseDTO);
		return new ResponseEntity<ApiResponseDTO<PagedResponseDTO<PromoPriceResponseDTO>>>(response, HttpStatus.OK);
	}

	@PostMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createPromoPrice(@PathVariable String productId,
			@RequestBody CreatePromoPriceRequestDTO dto) {
		Product product = productApplicationService.findById(productId);
		PromoPrice promoPrice = productApplicationService.createPromoPrice(product, dto);
		var response = new ApiResponseDTO<PromoPrice>("Tạo giá khuyến mại của sản phẩm thành công", true,
				promoPrice);
		return new ResponseEntity<ApiResponseDTO<PromoPrice>>(response, HttpStatus.OK);
	}

}

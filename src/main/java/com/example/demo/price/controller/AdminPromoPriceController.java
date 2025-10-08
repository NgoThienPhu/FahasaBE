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

import com.example.demo.price.dto.CreatePromoPriceRequestDTO;
import com.example.demo.price.dto.PromoPriceResponseDTO;
import com.example.demo.price.entity.PromoPrice;
import com.example.demo.price.service.PromoPriceService;
import com.example.demo.product.flow.ProductPricingFlow;
import com.example.demo.util.base.dto.ApiResponseDTO;
import com.example.demo.util.base.dto.PagedResponseDTO;

@RestController
@RequestMapping("/api/admin/products/{productId}/promo-prices")
public class AdminPromoPriceController {

	private PromoPriceService promoPriceService;
	
	private ProductPricingFlow productPricingFlow;
	
	public AdminPromoPriceController(PromoPriceService promoPriceService, ProductPricingFlow productPricingFlow) {
		this.promoPriceService = promoPriceService;
		this.productPricingFlow = productPricingFlow;
	}

	@GetMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllPromoPrice(@PathVariable(required = false) String productId,
			@RequestParam(required = true, defaultValue = "startDate") String sortBy,
			@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<PromoPrice> promoPrices = promoPriceService.findAll(productId, sortBy, orderBy, page,
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
		PromoPrice promoPrice = productPricingFlow.createPromoPrice(productId, dto);
		var response = new ApiResponseDTO<PromoPrice>("Tạo giá khuyến mại của sản phẩm thành công", true,
				promoPrice);
		return new ResponseEntity<ApiResponseDTO<PromoPrice>>(response, HttpStatus.OK);
	}

}

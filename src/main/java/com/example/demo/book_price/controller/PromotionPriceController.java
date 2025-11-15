package com.example.demo.book_price.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.book_price.dto.CreatePromotionPriceDTO;
import com.example.demo.book_price.entity.PromotionPrice;
import com.example.demo.book_price.entity.SellPrice;
import com.example.demo.book_price.flow.CreateNewPromotionPriceFlow;
import com.example.demo.book_price.service.PromotionService;
import com.example.demo.util.dto.ApiResponseDTO;
import com.example.demo.util.validation.BindingResultUtil;

@RestController
@RequestMapping("/api/admin/books/{bookId}/promotion-prices")
public class PromotionPriceController {

	private CreateNewPromotionPriceFlow createNewPromotionPriceFlow;
	private PromotionService promotionService;

	public PromotionPriceController(CreateNewPromotionPriceFlow createNewPromotionPriceFlow,
			PromotionService promotionService) {
		this.createNewPromotionPriceFlow = createNewPromotionPriceFlow;
		this.promotionService = promotionService;
	}
	
	@PostMapping
	public ResponseEntity<?> createNewPromotionPrice(@PathVariable(name = "bookId", required = true) String bookId,
			@RequestBody CreatePromotionPriceDTO dto, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Thêm giá khuyến mại thất bại");
		if (responseError != null)
			return responseError;

		PromotionPrice promotionPrice = createNewPromotionPriceFlow.createNewPromotionPrice(bookId, dto.getAmount(), dto.getFrom(), dto.getTo());
		var response = new ApiResponseDTO<PromotionPrice>("Thêm giá khuyến mại thành công", true, promotionPrice);
		return new ResponseEntity<ApiResponseDTO<PromotionPrice>>(response, HttpStatus.CREATED);
	}

}

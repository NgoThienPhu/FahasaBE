package com.example.demo.book_price.controller;

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
import com.example.demo.book_price.flow.CreateNewPromotionPriceFlow;
import com.example.demo.book_price.service.PromotionService;
import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponseSuccessDTO;
import com.example.demo.util.validation.BindingResultUtil;

import jakarta.servlet.http.HttpServletRequest;

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
			@RequestBody CreatePromotionPriceDTO dto, HttpServletRequest request, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result,
				"Thêm giá khuyến mại thất bại", request.getRequestURI());
		if (responseError != null)
			return responseError;

		PromotionPrice promotionPrice = createNewPromotionPriceFlow.createNewPromotionPrice(bookId, dto.getAmount(),
				dto.getFrom(), dto.getTo());
		var response = new ApiResponseSuccessDTO<PromotionPrice>(201, "Tạo giá khuyến mại thành công", promotionPrice);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.CREATED);
	}

}

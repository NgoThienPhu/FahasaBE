package com.example.demo.book_price.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.book_price.dto.CreateBookBasePrice;
import com.example.demo.book_price.entity.BasePrice;
import com.example.demo.book_price.service.BasePriceService;
import com.example.demo.util.response.ResponseFactory;

@RestController
@RequestMapping("/api/admin/books/{bookId}/base-prices")
public class BasePriceController {

	private BasePriceService basePriceService;
	private ResponseFactory responseFactory;

	public BasePriceController(BasePriceService basePriceService, ResponseFactory responseFactory) {
		this.basePriceService = basePriceService;
		this.responseFactory = responseFactory;
	}

	@GetMapping
	public ResponseEntity<?> getBasePrices(@PathVariable(required = true) String bookId,
			@RequestParam(required = false, defaultValue = "desc") String orderBy,
			@RequestParam(required = false, defaultValue = "effectiveFrom") String sortBy,
			@RequestParam(required = false) LocalDateTime effectiveFrom,
			@RequestParam(required = false) LocalDateTime effectiveTo,
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "10") int size) {
		List<BasePrice> basePrices = basePriceService.getBasePrices(bookId, orderBy, sortBy, effectiveFrom, effectiveTo,
				page, size);
		return responseFactory.success(basePrices, "Lấy giá cơ bản thành công");
	}

	@PostMapping
	public ResponseEntity<?> createBasePrice(@PathVariable String bookId, @RequestBody CreateBookBasePrice dto) {
		BasePrice basePrice = basePriceService.createBasePrice(bookId, dto);
		return responseFactory.success(basePrice, "Tạo giá cơ bản thành công",
				org.springframework.http.HttpStatus.CREATED);
	}

}
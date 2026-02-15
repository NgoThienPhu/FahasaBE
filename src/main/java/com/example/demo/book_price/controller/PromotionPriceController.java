package com.example.demo.book_price.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.book_price.service.PromotionService;

@RestController
@RequestMapping("/api/admin/books/{bookId}/promotion-prices")
public class PromotionPriceController {

	private PromotionService promotionService;

}

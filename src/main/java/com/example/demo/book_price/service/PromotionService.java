package com.example.demo.book_price.service;

import org.springframework.stereotype.Service;

import com.example.demo.book_price.repository.PromotionPriceRepository;

@Service
public class PromotionService {

	private PromotionPriceRepository promotionPriceRepository;

	public PromotionService(PromotionPriceRepository promotionPriceRepository) {
		this.promotionPriceRepository = promotionPriceRepository;
	}

}

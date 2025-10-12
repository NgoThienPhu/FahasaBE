package com.example.demo.book_price.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.book_price.entity.PromotionPrice;
import com.example.demo.book_price.repository.PromotionPriceRepository;

@Service
public class PromotionService {

	private PromotionPriceRepository promotionPriceRepository;

	public PromotionService(PromotionPriceRepository promotionPriceRepository) {
		this.promotionPriceRepository = promotionPriceRepository;
	}

	public PromotionPrice getCurrentPromotionPriceByProductId(String productId) {
		return promotionPriceRepository.getCurrentPromotionPrice(productId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Không tìm thấy giá khuyến mại hiện tại của sản phẩm"));
	}

}

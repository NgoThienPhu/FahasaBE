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

	public PromotionPrice createNewPromotionPrice(PromotionPrice promotionPrice) {
		boolean isOverlap = promotionPriceRepository.isOverlap(promotionPrice.getBook().getId(),
				promotionPrice.getEffectiveFrom(), promotionPrice.getEffectiveTo());
		if (isOverlap)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Xung đột thời gian, vui lòng chọn khoảng thời gian khác");

		return promotionPriceRepository.save(promotionPrice);
	}

}

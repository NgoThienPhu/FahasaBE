package com.example.demo.price.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.example.demo.price.entity.PromoPrice;

public interface PromoPriceService {
	
	PromoPrice save(PromoPrice promoPrice);

	Page<PromoPrice> findAll(String productId, String sortBy, String orderBy, int page, int size);

	PromoPrice getCurrentPromoPrice(String productId);

	boolean existsOverlap(String productId, LocalDateTime startDate, LocalDateTime endDate);
}

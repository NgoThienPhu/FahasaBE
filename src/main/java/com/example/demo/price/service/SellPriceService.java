package com.example.demo.price.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;

import com.example.demo.price.entity.SellPrice;

public interface SellPriceService {
	
	SellPrice save(SellPrice sellPrice);
	
	Page<SellPrice> findAll(String productId, String sortBy, String orderBy, int page, int size);

	SellPrice getCurrentSellPrice(String productId);
	
	SellPrice updateProductSellPrice(String productId, BigDecimal newSellPrice);
	
}

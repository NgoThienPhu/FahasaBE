package com.example.demo.price.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;

import com.example.demo.price.entity.PurchasePrice;

public interface PurchasePriceService {
	
	PurchasePrice save(PurchasePrice purchasePrice);

	PurchasePrice getCurrentPurchasePrice(String productId);

	Page<PurchasePrice> findAll(String productId, String sortBy, String orderBy, int page, int size);

	PurchasePrice update(String productId, BigDecimal newPurchasePrice);

}

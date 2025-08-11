package com.example.demo.services.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.example.demo.dto.price.CreatePromoPriceRequestDTO;
import com.example.demo.entities.base.ProductPrice;
import com.example.demo.entities.price.PromoPrice;
import com.example.demo.entities.price.PurchasePrice;
import com.example.demo.entities.price.SellPrice;

public interface ProductPriceService {
	
	ProductPrice save(ProductPrice productPrice);
	
	SellPrice getProductCurrentSellPrice(String productId);
	
	Page<SellPrice> getAllSellPriceByProduct(String productId, String sortBy, String orderBy, int page, int size);
	
	PromoPrice getProductCurrentPromoPrice(String productId);
	
	Page<PromoPrice> getAllPromoPriceByProduct(String productId, String sortBy, String orderBy, int page, int size);
	
	PurchasePrice getProductCurrentPurchasePrice(String productId);
	
	Page<PurchasePrice> getAllPurchasePriceByProduct(String productId, String sortBy, String orderBy, int page, int size);
	
	Boolean existsOverlappingPromotion(String productId, LocalDateTime startDate, LocalDateTime endDate);
	
	SellPrice updateProductSellPrice(String productId, BigDecimal newSellPrice);
	
	PurchasePrice updateProductPurchasePrice(String productId, BigDecimal newPurchasePrice);
	
	PromoPrice createProductPromoPrice(String productId, CreatePromoPriceRequestDTO dto);
	
}

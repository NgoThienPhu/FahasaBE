package com.example.demo.services.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.entities.common.ProductPrice;
import com.example.demo.entities.price.PromoPrice;
import com.example.demo.entities.price.SellPrice;

public interface ProductPriceService {
	
	ProductPrice save(ProductPrice productPrice);
	
	SellPrice getProductCurrentSellPrice(String productId);
	
	PromoPrice getProductCurrentPromoPrice(String productId);
	
	Boolean existsOverlappingPromotion(String productId, LocalDateTime startDate, LocalDateTime endDate);
	
	SellPrice updateProductSellPrice(String productId, BigDecimal newSellPrice);
	
}

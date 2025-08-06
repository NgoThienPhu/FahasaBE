package com.example.demo.services.interfaces;

import com.example.demo.entities.common.ProductPrice;

public interface ProductPriceService {
	
	ProductPrice save(ProductPrice productPrice);
	
	ProductPrice getProductCurrentSellPrice(String productId);
	
	ProductPrice getProductCurrentPromoPrice(String productId);
	
}

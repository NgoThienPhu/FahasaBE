package com.example.demo.productimage.service;

import com.example.demo.productimage.entity.ProductImage;

public interface ProductImageService {
	
	ProductImage findById(String productImageId);
	
	ProductImage save(ProductImage productImage);
	
	void deleteById(String productImageId);
	
}

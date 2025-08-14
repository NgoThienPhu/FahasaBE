package com.example.demo.product.service;

import org.springframework.data.domain.Page;

import com.example.demo.category.entity.Category;
import com.example.demo.product.entity.Product;

public interface ProductService {
	
	Product findById(String productId);
	
	Page<Product> findAll(String orderBy, String sortBy, int page, int size);
	
	Product save(Product product);
	
	Product update(String productId, String productName, String description, Category category);
	
	void deleteById(String productId);

}

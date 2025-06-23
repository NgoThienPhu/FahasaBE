package com.example.demo.services.interfaces;

import java.util.List;

import com.example.demo.entities.Product;

public interface ProductServiceInf {
	
	public Product createProduct(Product product);
	
	public Product updateProduct();
	
	public void deleteById(String productId);
	
	public List<Product> findProductsByCateogryId(String categoryId);
	
}

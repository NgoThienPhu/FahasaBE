package com.example.demo.services.implement;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.interfaces.ProductServiceInf;

@Service
public class ProductService implements ProductServiceInf {
	
	private ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public List<Product> findProductsByCateogryId(String categoryId) {
		return productRepository.findByCategoryId(categoryId);
	}

	@Override
	public Product createProduct(Product product) {
		return null;
	}

	@Override
	public Product updateProduct() {
		return null;
	}

	@Override
	public void deleteById(String productId) {
		
	}

}

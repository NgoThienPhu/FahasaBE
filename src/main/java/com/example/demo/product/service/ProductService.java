package com.example.demo.product.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.category.entity.Category;
import com.example.demo.product.dto.UpdateProductRequestDTO;
import com.example.demo.product.entity.Product;

public interface ProductService {
	
	Product save(Product product);

	Page<Product> findAll(String orderBy, String sortBy, int page, int size);

	Product update(String productId, String productName, String description, Category category);

	void deleteById(String productId);

	Product findById(String productId);

	Product update(String productId, UpdateProductRequestDTO dto);

	Product updatePrimaryImage(String productId, MultipartFile primaryImage);

	Product updateSecondImage(String productId, List<MultipartFile> secondImages);

	Product deleteSecondImage(String productId, List<String> secondImageIds);

}
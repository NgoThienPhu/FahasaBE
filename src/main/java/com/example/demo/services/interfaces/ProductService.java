package com.example.demo.services.interfaces;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ProductRequestDTO;
import com.example.demo.entities.Product;

public interface ProductService {

	Product findById(String productId);

	Product createProduct(ProductRequestDTO product, MultipartFile mainImage, List<MultipartFile> images)
			throws IOException;

	Product updateProduct();

	void deleteById(String productId);

	Boolean existsProductsByCategoryId(String categoryId);

}

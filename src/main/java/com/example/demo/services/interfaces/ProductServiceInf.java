package com.example.demo.services.interfaces;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ProductRequestDTO;
import com.example.demo.entities.Product;

public interface ProductServiceInf {

	public Product createProduct(ProductRequestDTO product, MultipartFile mainImage, List<MultipartFile> images)
			throws IOException;

	public Product updateProduct();

	public void deleteById(String productId);

	public List<Product> findProductsByCateogryId(String categoryId);

}

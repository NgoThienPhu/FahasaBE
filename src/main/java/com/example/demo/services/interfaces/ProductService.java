package com.example.demo.services.interfaces;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ProductFilterDTO;
import com.example.demo.dto.UpdateProductRequestDTO;
import com.example.demo.dto.CreateProductRequestDTO;
import com.example.demo.entities.Product;

public interface ProductService {

	Page<Product> findAll(ProductFilterDTO productFilterDTO, String orderBy, String sortBy, int page, int size);

	Product findById(String productId);

	Product createProduct(CreateProductRequestDTO product, MultipartFile mainImage, List<MultipartFile> images)
			throws IOException;

	Product createProduct(CreateProductRequestDTO product, MultipartFile mainImage) throws IOException;

	Product updateProduct(String productId, UpdateProductRequestDTO updateProductRequestDTO);

	void deleteById(String productId);

	Boolean existsProductsByCategoryId(String categoryId);
	
	Product updateNewMainImage(String productId, MultipartFile newMainImage);

}

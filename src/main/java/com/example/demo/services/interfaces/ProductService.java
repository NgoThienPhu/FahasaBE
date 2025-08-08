package com.example.demo.services.interfaces;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.product.CreateProductRequestDTO;
import com.example.demo.dto.product.ProductFilterDTO;
import com.example.demo.dto.product.ProductResponseDTO;
import com.example.demo.dto.product.UpdateProductRequestDTO;
import com.example.demo.entities.Product;

public interface ProductService {
	
	Product save(Product product);

	Page<ProductResponseDTO> findAll(ProductFilterDTO productFilterDTO, String orderBy, String sortBy, int page, int size);

	ProductResponseDTO getProductResponseById(String productId);

	Product getProductEntityById(String productId);

	ProductResponseDTO createProduct(CreateProductRequestDTO product, MultipartFile mainImage, List<MultipartFile> images)
			throws IOException;

	Product updateProduct(String productId, UpdateProductRequestDTO updateProductRequestDTO);

	void deleteById(String productId);

	Boolean existsProductsByCategoryId(String categoryId);

}

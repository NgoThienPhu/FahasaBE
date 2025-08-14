package com.example.demo.product.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.category.entity.Category;
import com.example.demo.product.entity.Product;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.product.service.ProductService;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;

	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	@Override
	public Product save(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Page<Product> findAll(String orderBy, String sortBy, int page, int size) {
		List<String> allowedFields = List.of("name", "price");

		if (!allowedFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}

		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);

		return productRepository.findAll(pageable);
	}

	@Transactional
	@Override
	public void deleteById(String productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("Không tìm thấy sản phẩm với Id là: %s", productId)));

		productRepository.delete(product);
	}

	@Override
	public Product findById(String productId) {
		return productRepository.findById(productId).orElse(null);
	}

	@Override
	public Product update(String productId, String productName, String description, Category category) {
		
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("Không tìm thấy sản phẩm với Id là: %s", productId)));
		
		if(category == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy loại sản phẩm, vui lòng thử lại sau");
		
		product.setCategory(category);
		if(productName != null) product.setName(productName);
		if(description != null) product.setDescription(description);
		
		return save(product);
	}

}

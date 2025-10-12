package com.example.demo.book_image.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.book_image.entity.BookImage;
import com.example.demo.book_image.repository.ProductImageRepository;
import com.example.demo.book_image.service.ProductImageService;

@Service
public class ProductImageService {

	private ProductImageRepository productImageRepository;

	public ProductImageService(ProductImageRepository productImageRepository) {
		this.productImageRepository = productImageRepository;
	}

	public BookImage findById(String productImageId) {
		return productImageRepository.findById(productImageId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ảnh không tồn tại"));
	}

	@Transactional
	public BookImage save(BookImage productImage) {
		return productImageRepository.save(productImage);
	}

	public void deleteById(String productImageId) {
		BookImage productImage = findById(productImageId);
		productImageRepository.delete(productImage);
	}

}

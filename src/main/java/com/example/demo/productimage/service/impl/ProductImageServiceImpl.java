package com.example.demo.productimage.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.productimage.entity.ProductImage;
import com.example.demo.productimage.repository.ProductImageRepository;
import com.example.demo.productimage.service.ProductImageService;

@Service
public class ProductImageServiceImpl implements ProductImageService {

	private ProductImageRepository productImageRepository;

	public ProductImageServiceImpl(ProductImageRepository productImageRepository) {
		this.productImageRepository = productImageRepository;
	}

	@Override
	public ProductImage findById(String productImageId) {
		return productImageRepository.findById(productImageId).orElse(null);
	}

	@Transactional
	@Override
	public ProductImage save(ProductImage productImage) {
		return productImageRepository.save(productImage);
	}

	@Override
	public void deleteById(String productImageId) {

		ProductImage productImage = findById(productImageId);

		if (productImage == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hình ảnh không tồn tại vui lòng thử lại sau");

		productImageRepository.delete(productImage);
	}

}

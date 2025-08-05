package com.example.demo.services.implement;

import org.springframework.stereotype.Service;

import com.example.demo.entities.bases.ProductPrice;
import com.example.demo.repository.ProductPriceRepository;
import com.example.demo.services.interfaces.ProductPriceService;

@Service
public class ProductPriceServiceImpl implements ProductPriceService {
	
	private ProductPriceRepository productPriceRepository;

	public ProductPriceServiceImpl(ProductPriceRepository productPriceRepository) {
		this.productPriceRepository = productPriceRepository;
	}

	@Override
	public ProductPrice save(ProductPrice productPrice) {
		return productPriceRepository.save(productPrice);
	}

	
	
}

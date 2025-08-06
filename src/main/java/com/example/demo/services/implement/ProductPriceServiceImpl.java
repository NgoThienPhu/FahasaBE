package com.example.demo.services.implement;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.entities.price.ProductPrice;
import com.example.demo.entities.price.PromoPrice;
import com.example.demo.entities.price.SellPrice;
import com.example.demo.repository.ProductPriceRepository;
import com.example.demo.services.interfaces.ProductPriceService;
import com.example.demo.specification.ProductPriceSpecification;

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

	@Override
	public ProductPrice getProductCurrentSellPrice(String productId) {
		Specification<ProductPrice> fallbackSpec = Specification
	            .where(ProductPriceSpecification.hasProductId(productId))
	            .and(ProductPriceSpecification.isActive())
	            .and(ProductPriceSpecification.hasType(SellPrice.class));

	        return productPriceRepository.findOne(fallbackSpec).orElse(null);
	}

	@Override
	public ProductPrice getProductCurrentPromoPrice(String productId) {
		Specification<ProductPrice> spec = Specification.where(ProductPriceSpecification.hasProductId(productId))
				.and(ProductPriceSpecification.isActive())
				.and(ProductPriceSpecification.hasType(PromoPrice.class));
		
		List<ProductPrice> promoPrices = productPriceRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "startDate"));
		
		return promoPrices.stream().findFirst().orElse(null);
	}

	
	
}

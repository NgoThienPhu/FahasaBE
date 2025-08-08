package com.example.demo.services.implement;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.common.ProductPrice;
import com.example.demo.entities.price.PromoPrice;
import com.example.demo.entities.price.SellPrice;
import com.example.demo.repositories.ProductPriceRepository;
import com.example.demo.services.interfaces.ProductPriceService;
import com.example.demo.specifications.ProductPriceSpecification;

@Service
public class ProductPriceServiceImpl implements ProductPriceService {
	
	private ProductPriceRepository productPriceRepository;

	public ProductPriceServiceImpl(ProductPriceRepository productPriceRepository) {
		this.productPriceRepository = productPriceRepository;
	}

	@Transactional
	@Override
	public ProductPrice save(ProductPrice productPrice) {
		return productPriceRepository.save(productPrice);
	}

	@Override
	public SellPrice getProductCurrentSellPrice(String productId) {
		Specification<ProductPrice> fallbackSpec = Specification
	            .where(ProductPriceSpecification.hasProductId(productId))
	            .and(ProductPriceSpecification.isActive())
	            .and(ProductPriceSpecification.hasType(SellPrice.class));

	        return (SellPrice) productPriceRepository.findOne(fallbackSpec).orElse(null);
	}

	@Override
	public PromoPrice getProductCurrentPromoPrice(String productId) {
		Specification<ProductPrice> spec = Specification.where(ProductPriceSpecification.hasProductId(productId))
				.and(ProductPriceSpecification.isActive())
				.and(ProductPriceSpecification.hasType(PromoPrice.class));
		
		List<ProductPrice> promoPrices = productPriceRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "startDate"));
		
		return (PromoPrice) promoPrices.stream().findFirst().orElse(null);
	}

	@Override
	public Boolean existsOverlappingPromotion(String productId, LocalDateTime startDate, LocalDateTime endDate) {
		Specification<ProductPrice> spec = Specification.where(ProductPriceSpecification.hasProductId(productId))
				.and(ProductPriceSpecification.hasType(PromoPrice.class))
				.and(ProductPriceSpecification.hashasOverlap(startDate, endDate));
		
		return productPriceRepository.count(spec) > 0;
	}

	@Override
	public SellPrice updateProductSellPrice(String productId, BigDecimal newPrice) {
		
		if(newPrice.compareTo(BigDecimal.valueOf(1_000)) < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giá của sản phẩm phải lớn hơn 1.000 VNĐ");
		}
		
		SellPrice currentSellPrice = getProductCurrentSellPrice(productId);
		
		if(currentSellPrice != null) {
			currentSellPrice.setEndDate(LocalDateTime.now());
			productPriceRepository.save(currentSellPrice);
		}
		
		SellPrice newSellPrice = new SellPrice(currentSellPrice.getProduct(), newPrice);
		
		return (SellPrice) productPriceRepository.save(newSellPrice);
	}

	
	
}

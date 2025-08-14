package com.example.demo.price.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.price.entity.SellPrice;
import com.example.demo.price.repository.SellPriceRepository;
import com.example.demo.price.service.SellPriceService;
import com.example.demo.price.specification.SellPriceSpecification;

@Service
public class SellPriceServiceImpl implements SellPriceService {

	private SellPriceRepository sellPriceRepository;

	public SellPriceServiceImpl(SellPriceRepository sellPriceRepository) {
		this.sellPriceRepository = sellPriceRepository;
	}
	
	@Override
	public SellPrice save(SellPrice sellPrice) {
		return sellPriceRepository.save(sellPrice);
	}

	@Override
	public Page<SellPrice> findAll(String productId, String sortBy, String orderBy, int page, int size) {
		List<String> allowedFields = List.of("startDate", "price");
		if (!allowedFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}
		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);
		Pageable pageable = PageRequest.of(page, size, sort);
		return sellPriceRepository.findAll(SellPriceSpecification.hasProductId(productId), pageable);
	}

	@Override
	public SellPrice getCurrentSellPrice(String productId) {
		Specification<SellPrice> fallbackSpec = Specification.where(SellPriceSpecification.hasProductId(productId))
				.and(SellPriceSpecification.isActive());

		return sellPriceRepository.findOne(fallbackSpec).orElse(null);
	}

	@Override
	public SellPrice updateProductSellPrice(String productId, BigDecimal newSellPrice) {
		if (newSellPrice.compareTo(BigDecimal.valueOf(1_000)) < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giá của sản phẩm phải lớn hơn 1.000 VNĐ");
		}

		SellPrice currentSellPrice = getCurrentSellPrice(productId);

		if (currentSellPrice != null) {
			currentSellPrice.setEndDate(LocalDateTime.now());
			sellPriceRepository.save(currentSellPrice);
		}

		SellPrice sellPrice = new SellPrice(currentSellPrice.getProduct(), newSellPrice);

		return sellPriceRepository.save(sellPrice);
	}
	
}

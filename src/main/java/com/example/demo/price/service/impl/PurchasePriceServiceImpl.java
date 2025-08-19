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

import com.example.demo.price.entity.PurchasePrice;
import com.example.demo.price.repository.PurchasePriceRepository;
import com.example.demo.price.service.PurchasePriceService;
import com.example.demo.price.specification.PurchasePriceSpecification;

@Service
public class PurchasePriceServiceImpl implements PurchasePriceService {

	private PurchasePriceRepository purchasePriceRepository;

	public PurchasePriceServiceImpl(PurchasePriceRepository purchasePriceRepository) {
		this.purchasePriceRepository = purchasePriceRepository;
	}
	
	@Override
	public PurchasePrice save(PurchasePrice purchasePrice) {
		return purchasePriceRepository.save(purchasePrice);
	}

	@Override
	public PurchasePrice getCurrentPurchasePrice(String productId) {
		Specification<PurchasePrice> spec = Specification.where(PurchasePriceSpecification.hasProductId(productId))
				.and(PurchasePriceSpecification.isActive());

		return purchasePriceRepository.findOne(spec).orElse(null);
	}

	@Override
	public Page<PurchasePrice> findAll(String productId, String sortBy, String orderBy, int page, int size) {
		List<String> allowedFields = List.of("startDate", "price");

		if (!allowedFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}

		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);

		return purchasePriceRepository.findAll(PurchasePriceSpecification.hasProductId(productId), pageable);
	}

	@Override
	public PurchasePrice updatePurchasePrice(String productId, BigDecimal newPurchasePrice) {
		if (newPurchasePrice.compareTo(BigDecimal.valueOf(1_000)) < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giá của sản phẩm phải lớn hơn 1.000 VNĐ");
		}

		PurchasePrice currentPurchasePrice = getCurrentPurchasePrice(productId);

		if (currentPurchasePrice != null) {
			currentPurchasePrice.setEndDate(LocalDateTime.now());
			purchasePriceRepository.save(currentPurchasePrice);
		}

		PurchasePrice newSellPrice = new PurchasePrice(currentPurchasePrice.getProduct(), newPurchasePrice);

		return purchasePriceRepository.save(newSellPrice);
	}

}

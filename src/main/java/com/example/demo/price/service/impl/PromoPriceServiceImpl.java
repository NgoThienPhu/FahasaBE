package com.example.demo.price.service.impl;

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

import com.example.demo.price.dto.CreatePromoPriceRequestDTO;
import com.example.demo.price.entity.PromoPrice;
import com.example.demo.price.repository.PromoPriceRepository;
import com.example.demo.price.service.PromoPriceService;
import com.example.demo.price.specification.PromoPriceSpecification;
import com.example.demo.product.entity.Product;

@Service
public class PromoPriceServiceImpl implements PromoPriceService {

	private PromoPriceRepository promoPriceRepository;

	public PromoPriceServiceImpl(PromoPriceRepository promoPriceRepository) {
		this.promoPriceRepository = promoPriceRepository;
	}
	
	@Override
	public PromoPrice save(PromoPrice promoPrice) {
		return promoPriceRepository.save(promoPrice);
	}

	@Override
	public Page<PromoPrice> findAll(String productId, String sortBy, String orderBy, int page, int size) {
		List<String> allowedFields = List.of("startDate", "price");

		if (!allowedFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}

		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);

		return promoPriceRepository.findAll(PromoPriceSpecification.hasProductId(productId), pageable);
	}

	@Override
	public PromoPrice create(Product product, CreatePromoPriceRequestDTO dto) {

		if (dto.endDate().isBefore(dto.startDate())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày kết thúc phải sau ngày bắt đầu");
		}

		Boolean checkOverlap = existsOverlap(product.getId(), dto.startDate(), dto.endDate());

		if (checkOverlap)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Đã có khuyến mại được thiết lập trong khoảng thời gian này");

		PromoPrice promoPrice = new PromoPrice(product, dto.promoName(), dto.promoPrice(), dto.startDate(),
				dto.endDate());

		return promoPriceRepository.save(promoPrice);
	}

	@Override
	public PromoPrice getCurrentPromoPrice(String productId) {
		Specification<PromoPrice> spec = Specification.where(PromoPriceSpecification.hasProductId(productId))
				.and(PromoPriceSpecification.isActive());

		List<PromoPrice> promoPrices = promoPriceRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "startDate"));

		return promoPrices.stream().findFirst().orElse(null);
	}

	@Override
	public boolean existsOverlap(String productId, LocalDateTime startDate, LocalDateTime endDate) {
		Specification<PromoPrice> spec = Specification.where(PromoPriceSpecification.hasProductId(productId))
				.and(PromoPriceSpecification.hasOverlap(startDate, endDate));

		return promoPriceRepository.count(spec) > 0;
	}

}

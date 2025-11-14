package com.example.demo.book_price.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.book_price.entity.SellPrice;
import com.example.demo.book_price.repository.SellPriceRepository;

@Service
public class SellPriceService {

	private SellPriceRepository sellPriceRepository;

	public SellPriceService(SellPriceRepository sellPriceRepository) {
		this.sellPriceRepository = sellPriceRepository;
	}

	public SellPrice createNewsellPrice(SellPrice sellPrice) {
		boolean isOverlap = sellPriceRepository.isOverlap(sellPrice.getBook().getId(), sellPrice.getEffectiveFrom());
		if (isOverlap)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Xung đột thời gian, vui lòng chọn khoảng thời gian khác");

		SellPrice previousPrice = sellPriceRepository.findPreviousPrice(sellPrice.getBook().getId(),
				sellPrice.getEffectiveFrom());
		previousPrice.setEffectiveTo(sellPrice.getEffectiveFrom().minusSeconds(1));
		sellPriceRepository.save(previousPrice);

		return sellPriceRepository.save(sellPrice);
	}

}

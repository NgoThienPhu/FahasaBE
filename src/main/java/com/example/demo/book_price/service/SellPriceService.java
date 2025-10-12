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

	public SellPrice getCurrentSellPriceByProductId(String productId) {
		return sellPriceRepository.getCurrentSellPrice(productId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Không tìm thấy giá bán hiện tại của sản phẩm"));
	}

}

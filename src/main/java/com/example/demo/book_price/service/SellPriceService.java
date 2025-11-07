package com.example.demo.book_price.service;

import org.springframework.stereotype.Service;
import com.example.demo.book_price.repository.SellPriceRepository;

@Service
public class SellPriceService {

	private SellPriceRepository sellPriceRepository;

	public SellPriceService(SellPriceRepository sellPriceRepository) {
		this.sellPriceRepository = sellPriceRepository;
	}

}

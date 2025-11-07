package com.example.demo.book_price.service;

import org.springframework.stereotype.Service;

import com.example.demo.book_price.repository.ImportPriceRepository;

@Service
public class ImportPriceService {

	private ImportPriceRepository importPriceRepository;

	public ImportPriceService(ImportPriceRepository importPriceRepository) {
		this.importPriceRepository = importPriceRepository;
	}

}

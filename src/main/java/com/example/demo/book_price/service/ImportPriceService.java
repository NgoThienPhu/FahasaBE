package com.example.demo.book_price.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.book_price.entity.ImportPrice;
import com.example.demo.book_price.repository.ImportPriceRepository;

@Service
public class ImportPriceService {

	private ImportPriceRepository importPriceRepository;

	public ImportPriceService(ImportPriceRepository importPriceRepository) {
		this.importPriceRepository = importPriceRepository;
	}
	
	public ImportPrice createNewImportPrice(ImportPrice importPrice) {
		boolean isOverlap = importPriceRepository.isOverlap(importPrice.getBook().getId(), importPrice.getEffectiveFrom());
		if(isOverlap) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Xung đột thời gian, vui lòng chọn khoảng thời gian khác");
		
		ImportPrice previousPrice = importPriceRepository.findPreviousPrice(importPrice.getBook().getId(), importPrice.getEffectiveFrom());
		previousPrice.setEffectiveTo(importPrice.getEffectiveFrom().minusSeconds(1));
		importPriceRepository.save(previousPrice);
		
		return importPriceRepository.save(importPrice);
	}

}

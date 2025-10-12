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

	public ImportPrice getCurrentImportPriceByProductId(String productId) {
		return importPriceRepository.getCurrentImportPrice(productId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Không tìm thấy giá nhập hiện tại của sản phẩm"));
	}

}

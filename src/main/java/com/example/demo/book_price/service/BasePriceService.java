package com.example.demo.book_price.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.book.entity.Book;
import com.example.demo.book.repository.BookRepository;
import com.example.demo.book_price.dto.CreateBookBasePrice;
import com.example.demo.book_price.entity.BasePrice;
import com.example.demo.book_price.repository.BasePriceRepository;
import com.example.demo.util.exception.CustomException;

@Service
public class BasePriceService {

	private BasePriceRepository basePriceRepository;
	private BookRepository bookRepository;

	public BasePriceService(BasePriceRepository basePriceRepository, BookRepository bookRepository) {
		this.basePriceRepository = basePriceRepository;
		this.bookRepository = bookRepository;
	}

	public List<BasePrice> getBasePrices(String bookId, String orderBy, String sortBy, LocalDateTime fromDate,
			LocalDateTime toDate, int page, int size) {
		List<String> allowedFields = List.of("effectiveFrom", "effectiveTo", "price");
		if (!allowedFields.contains(sortBy)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Trường sắp xếp không hợp lệ");
		}

		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);

		return basePriceRepository.findByBookId(bookId, fromDate, toDate, pageable);
	}

	public BasePrice createBasePrice(String bookId, CreateBookBasePrice dto) {
		boolean hasOverlap = basePriceRepository.existsOverlappingActivePrice(bookId, dto.effectiveFrom(),
				dto.effectiveFrom());
		if (hasOverlap) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Xung đột thời gian với giá hiện tại");
		}

		BasePrice currentPrice = basePriceRepository.findCurrentPrice(bookId, LocalDateTime.now()).orElse(null);
		Book book = findBookById(bookId);
		if (currentPrice != null) {
			BasePrice basePrice = new BasePrice(book, dto.price(), dto.effectiveFrom());

			currentPrice.setEffectiveTo(dto.effectiveFrom().minusSeconds(1));
			basePriceRepository.save(currentPrice);

			return basePriceRepository.save(basePrice);
		} else {
			BasePrice basePrice = new BasePrice(book, dto.price(), dto.effectiveFrom());
			return basePriceRepository.save(basePrice);
		}

	}

	private Book findBookById(String bookId) {
		return bookRepository.findById(bookId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Sách không tồn tại"));
	}

}

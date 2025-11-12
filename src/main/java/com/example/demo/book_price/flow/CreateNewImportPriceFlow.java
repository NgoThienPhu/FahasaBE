package com.example.demo.book_price.flow;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.book.entity.Book;
import com.example.demo.book.service.BookService;
import com.example.demo.book_price.entity.ImportPrice;
import com.example.demo.book_price.service.ImportPriceService;

@Service
public class CreateNewImportPriceFlow {
	
	private ImportPriceService importPriceService;
	private BookService bookService;
	
	public CreateNewImportPriceFlow(ImportPriceService importPriceService, BookService bookService) {
		this.importPriceService = importPriceService;
		this.bookService = bookService;
	}
	
	public ImportPrice createNewImportPrice(String bookId, BigDecimal amount, LocalDateTime from) {
		Book book = bookService.findById(bookId);
		if(book == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sách không tồn tại");
		
		ImportPrice importPrice = new ImportPrice();
		importPrice.setBook(book);
		importPrice.setAmount(amount);
		importPrice.setEffectiveFrom(from);
		importPrice.setEffectiveTo(null);
		
		return importPriceService.createNewImportPrice(importPrice);
	}

}

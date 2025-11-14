package com.example.demo.book_price.flow;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.book.entity.Book;
import com.example.demo.book.service.BookService;
import com.example.demo.book_price.entity.SellPrice;
import com.example.demo.book_price.service.SellPriceService;

@Service
public class CreateNewSellPriceFlow {
	
	private SellPriceService sellPriceService;
	private BookService bookService;
	
	public CreateNewSellPriceFlow(SellPriceService sellPriceService, BookService bookService) {
		this.sellPriceService = sellPriceService;
		this.bookService = bookService;
	}
	
	public SellPrice createNewSellPrice(String bookId, BigDecimal amount, LocalDateTime from) {
		Book book = bookService.findById(bookId);
		if(book == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sách không tồn tại");
		
		SellPrice sellPrice = new SellPrice();
		sellPrice.setBook(book);
		sellPrice.setAmount(amount);
		sellPrice.setEffectiveFrom(from);
		sellPrice.setEffectiveTo(null);
		
		return sellPriceService.createNewsellPrice(sellPrice);
	}

}

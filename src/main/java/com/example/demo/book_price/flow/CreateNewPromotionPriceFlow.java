package com.example.demo.book_price.flow;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.book.entity.Book;
import com.example.demo.book.service.BookService;
import com.example.demo.book_price.entity.PromotionPrice;
import com.example.demo.book_price.service.PromotionService;

@Service
public class CreateNewPromotionPriceFlow {
	
	private PromotionService promotionService;
	private BookService bookService;
	
	public CreateNewPromotionPriceFlow(PromotionService promotionService, BookService bookService) {
		this.promotionService = promotionService;
		this.bookService = bookService;
	}
	
	public PromotionPrice createNewPromotionPrice(String bookId, BigDecimal amount, LocalDateTime from, LocalDateTime to) {
		Book book = bookService.findById(bookId);
		if(book == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sách không tồn tại");
		
		if(to.isBefore(from)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thời gian kết thúc phải lớn hơn thời gian bắt đầu");
		
		PromotionPrice promotionPrice = new PromotionPrice();
		promotionPrice.setBook(book);
		promotionPrice.setAmount(amount);
		promotionPrice.setEffectiveFrom(from);
		promotionPrice.setEffectiveTo(to);
		
		return promotionService.createNewPromotionPrice(promotionPrice);
	}

}

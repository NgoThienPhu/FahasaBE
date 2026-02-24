package com.example.demo.book_price.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.book.entity.Book;
import com.example.demo.book_price.entity.base.BookPrice;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("BASE")
public class BasePrice extends BookPrice {
	
	public BasePrice(Book book, BigDecimal price, LocalDateTime effectiveFrom) {
		super(book, price, effectiveFrom, null);
	}

}

package com.example.demo.book.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo.book.entity.Book;
import com.example.demo.book_price.entity.base.BookPrice;
import com.example.demo.category.entity.Category;

public record BookResponseDTO(

		String id,
		String title, 
		String description, 
		String author, 
		String publisher, 
		String isbn, 
		Category category,
		LocalDate publishDate, 
		BookPrice price,
		LocalDateTime createdAt,
		LocalDateTime updatedAt

) {

	public static BookResponseDTO fromEntity(Book book, Category category, BookPrice price) {
		return new BookResponseDTO(
				book.getId(),
				book.getTitle(),
				book.getDescription(),
				book.getAuthor(),
				book.getPublisher(),
				book.getIsbn(),
				category,
				book.getPublishDate(),
				price,
				book.getCreatedAt(),
				book.getUpdatedAt()
		);
	}

}

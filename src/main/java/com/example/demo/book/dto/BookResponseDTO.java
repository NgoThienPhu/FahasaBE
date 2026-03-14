package com.example.demo.book.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo.book.entity.Book;
import com.example.demo.book_image.entity.BookImage;
import com.example.demo.book_price.entity.base.BookPrice;
import com.example.demo.category.entity.Category;

public record BookResponseDTO(

		String id,
		String title, 
		String summary,
		String description, 
		String author, 
		String publisher, 
		String isbn,
		BookImage primaryImage,
		Category category,
		LocalDate publishDate, 
		BookPrice price,
		LocalDateTime createdAt,
		LocalDateTime updatedAt

) {

	public static BookResponseDTO fromEntity(Book book, BookImage primaryImage, Category category, BookPrice price) {
		return new BookResponseDTO(
				book.getId(),
				book.getTitle(),
				book.getSummary(),
				book.getDescription(),
				book.getAuthor(),
				book.getPublisher(),
				book.getIsbn(),
				primaryImage,
				category,
				book.getPublishDate(),
				price,
				book.getCreatedAt(),
				book.getUpdatedAt()
		);
	}

}

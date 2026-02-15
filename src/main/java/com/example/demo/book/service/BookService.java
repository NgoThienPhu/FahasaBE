package com.example.demo.book.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.book.dto.CreateBookRequestDTO;
import com.example.demo.book.entity.Book;
import com.example.demo.book.repository.BookRepository;
import com.example.demo.book_image.entity.BookImage;
import com.example.demo.book_price.entity.ImportPrice;
import com.example.demo.book_price.entity.SellPrice;
import com.example.demo.category.entity.Category;
import com.example.demo.category.repository.CategoryRepository;
import com.example.demo.util.exception.CustomException;
import com.example.demo.util.service.S3Service;

@Service
public class BookService {

	private BookRepository bookRepository;
	private CategoryRepository categoryRepository;
	private S3Service s3Service;

	public Book createBook(CreateBookRequestDTO dto) {
		Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Category không tồn tại"));
		Book book = new Book();
		book.setTitle(dto.getTitle());
		book.setDescription(dto.getDescription());
		book.setAuthor(dto.getAuthor());
		book.setPublisher(dto.getPublisher());
		book.setIsbn(dto.getIsbn());
		book.setCategory(category);
		book.setPublishDate(dto.getPublishDate());

		String primaryImageUrl = s3Service.uploadFile(dto.getPrimaryImage());
		List<String> secondImageUrls = dto.getSecondImages().stream().map(s3Service::uploadFile).toList();

		BookImage primaryImage = new BookImage();
		primaryImage.setBook(book);
		primaryImage.setIsPrimary(true);
		primaryImage.setUrl(primaryImageUrl);

		List<BookImage> secondImages = secondImageUrls.stream().map(url -> {
			BookImage bookImage = new BookImage();
			bookImage.setBook(book);
			bookImage.setIsPrimary(false);
			bookImage.setUrl(url);
			return bookImage;
		}).collect(Collectors.toList());
		
		book.getBookImages().add(primaryImage);
		book.getBookImages().addAll(secondImages);
		
		ImportPrice importPrice = new ImportPrice();
		importPrice.setAmount(dto.getPrice());
		importPrice.setBook(book);
		importPrice.setEffectiveFrom(LocalDateTime.now());
		importPrice.setEffectiveTo(null);
		
		SellPrice sellPrice = new SellPrice();
		sellPrice.setAmount(dto.getPrice());
		sellPrice.setBook(book);
		sellPrice.setEffectiveFrom(LocalDateTime.now());
		sellPrice.setEffectiveTo(null);
		
		book.getBookPrices().add(importPrice);
		book.getBookPrices().add(sellPrice);
		
		return bookRepository.save(book);
	}

}

package com.example.demo.book_image.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.book_image.entity.BookImage;
import com.example.demo.book_image.service.BookImageService;
import com.example.demo.util.response.ResponseFactory;

@RestController
@RequestMapping("/api/books/{bookId}/images")
public class BookImageController {
	
	private BookImageService bookImageService;
	private ResponseFactory responseFactory;
	
	public BookImageController(BookImageService bookImageService, ResponseFactory responseFactory) {
		this.bookImageService = bookImageService;
		this.responseFactory = responseFactory;
	}
	
	@GetMapping("/secondary")
	public ResponseEntity<?> getBookSecondaryImages(@PathVariable String bookId) {
		List<BookImage> secondaryImage = bookImageService.getBookSecondaryImages(bookId);
		return responseFactory.success(secondaryImage, "Lấy ảnh phụ thành công");
	}

}
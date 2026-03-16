package com.example.demo.book_image.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.book_image.entity.BookImage;
import com.example.demo.book_image.service.BookImageService;
import com.example.demo.util.response.ApiResponse;
import com.example.demo.util.response.ApiResponseSuccess;

@RestController
@RequestMapping("/api/books/{bookId}/images")
public class BookImageController {
	
	private BookImageService bookImageService;
	
	public BookImageController(BookImageService bookImageService) {
		this.bookImageService = bookImageService;
	}
	
	@GetMapping("/secondary")
	public ResponseEntity<?> getBookSecondaryImages(@PathVariable String bookId) {
		List<BookImage> secondaryImage = bookImageService.getBookSecondaryImages(bookId);
		var response = new ApiResponseSuccess<List<BookImage>>(200, "Lấy ảnh phụ thành công", secondaryImage);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}

}

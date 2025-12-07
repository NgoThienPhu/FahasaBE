package com.example.demo.book.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.book.entity.Book;
import com.example.demo.book.service.BookService;
import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponsePaginationSuccess;

@RestController
@RequestMapping("/api/books")
public class BookController {

	private BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping
	public ResponseEntity<?> findAll(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		Page<Book> books = bookService.findAll(page, size);
		var response = new ApiResponsePaginationSuccess<List<Book>>(
				200, 
				"Lấy danh sách thành công", 
				books.getContent(), 
				books.getNumber(), 
				books.getSize(), 
				books.getTotalElements(), 
				books.getTotalPages()
		);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

}

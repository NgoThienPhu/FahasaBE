package com.example.demo.book.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.book.dto.BookResponseDTO;
import com.example.demo.book.service.BookService;
import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponsePaginationSuccess;
import com.example.demo.util.dto.api_response.ApiResponseSuccessDTO;

@RestController
@RequestMapping("/api/books")
public class BookController {

	private BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}
	
	@GetMapping
	public ResponseEntity<?> getBooks(@RequestParam(required = false) String search,
			@RequestParam(required = false, defaultValue = "asc") String orderBy,
			@RequestParam(required = false, defaultValue = "title") String sortBy,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<BookResponseDTO> bookPage = bookService.getBooks(search, orderBy, sortBy, page, size);
		ApiResponsePaginationSuccess<List<BookResponseDTO>> response = ApiResponsePaginationSuccess.fromPage(bookPage,
				"Lấy sách thành công");

		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}
	
	@GetMapping("/{bookId}")
	public ResponseEntity<?> getBookById(@PathVariable String bookId) {
		var book = bookService.getBookById(bookId);

		var response = new ApiResponseSuccessDTO<BookResponseDTO>(200, "Lấy sách thành công", book);

		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

}

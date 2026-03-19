package com.example.demo.book.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.book.dto.BookResponseDTO;
import com.example.demo.book.service.BookService;
import com.example.demo.util.response.ResponseFactory;
import com.example.demo.util.response.Pagination;

@RestController
@RequestMapping("/api/books")
public class BookController {

	private BookService bookService;
	private ResponseFactory responseFactory;

	public BookController(BookService bookService, ResponseFactory responseFactory) {
		this.bookService = bookService;
		this.responseFactory = responseFactory;
	}
	
	@GetMapping
	public ResponseEntity<?> getBooks(@RequestParam(required = false) String search,
				@RequestParam(required = false, defaultValue = "asc") String orderBy,
				@RequestParam(required = false, defaultValue = "title") String sortBy,
				@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<BookResponseDTO> bookPage = bookService.getBooks(search, orderBy, sortBy, page, size);
		Pagination pagination = new Pagination(bookPage.getNumber(), bookPage.getSize(), bookPage.getTotalElements(),
				bookPage.getTotalPages());
		return responseFactory.success(bookPage.getContent(), "Lấy sách thành công", pagination);
	}
	
	@GetMapping("/ids")
	public ResponseEntity<?> getBookByIds(@RequestParam(required = true) List<String> bookIds) {
		var books = bookService.getBookByIds(bookIds);
		return responseFactory.success(books, "Lấy danh sách sách thành công");
	}
	
	@GetMapping("/{bookId}")
	public ResponseEntity<?> getBookById(@PathVariable String bookId) {
		var book = bookService.getBookById(bookId);

		return responseFactory.success(book, "Lấy sách thành công");

	}

}
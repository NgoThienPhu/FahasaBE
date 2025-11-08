package com.example.demo.book.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.book.entity.Book;
import com.example.demo.book.service.BookService;
import com.example.demo.util.dto.ApiResponseDTO;
import com.example.demo.util.dto.PagedResponseDTO;

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
		PagedResponseDTO<Book> pageBooks = PagedResponseDTO.convertPageToPagedResponseDTO(books);
		var response = new ApiResponseDTO<PagedResponseDTO<Book>>("Lấy danh sách thành công", true, pageBooks);
		return new ResponseEntity<ApiResponseDTO<PagedResponseDTO<Book>>>(response, HttpStatus.OK);
	}

}

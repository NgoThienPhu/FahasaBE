package com.example.demo.book.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.book.dto.BookResponseDTO;
import com.example.demo.book.dto.CreateBookRequestDTO;
import com.example.demo.book.dto.UpdateBookRequestDTO;
import com.example.demo.book.service.BookService;
import com.example.demo.util.response.ResponseFactory;
import com.example.demo.util.response.Pagination;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/books")
public class AdminBookController {

	private BookService bookService;
	private ResponseFactory responseFactory;

	public AdminBookController(BookService bookService, ResponseFactory responseFactory) {
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

	@GetMapping("/{bookId}")
	public ResponseEntity<?> getBookById(@PathVariable String bookId) {
		var book = bookService.getBookById(bookId);
		return responseFactory.success(book, "Lấy sách thành công");
	}

	@PostMapping
	public ResponseEntity<?> createBook(@RequestBody @Valid CreateBookRequestDTO dto) {
		BookResponseDTO book = bookService.createBook(dto);
		return responseFactory.success(book, "Tạo sách thành công", org.springframework.http.HttpStatus.CREATED);
	}

	@PutMapping("/{bookId}")
	public ResponseEntity<?> updateBook(@PathVariable String bookId, @RequestBody @Valid UpdateBookRequestDTO dto) {

		BookResponseDTO book = bookService.updateBook(bookId, dto);
		return responseFactory.success(book, "Cập nhật sách thành công");
	}

	@DeleteMapping("/{bookId}")
	public ResponseEntity<?> deleteBook(@PathVariable String bookId) {
		bookService.deleteBook(bookId);
		return responseFactory.success("Xóa sách thành công");
	}

}
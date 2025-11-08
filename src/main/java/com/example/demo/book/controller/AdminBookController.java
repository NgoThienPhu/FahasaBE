package com.example.demo.book.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.book.dto.CreateBookRequestDTO;
import com.example.demo.book.entity.Book;
import com.example.demo.book.flow.CreateBookFlow;
import com.example.demo.book.service.BookService;
import com.example.demo.util.dto.ApiResponseDTO;
import com.example.demo.util.validation.BindingResultUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/books")
public class AdminBookController {
	
	private BookService bookService;
	private CreateBookFlow createBookFlow;
	
	public AdminBookController(BookService bookService, CreateBookFlow createBookFlow) {
		this.bookService = bookService;
		this.createBookFlow = createBookFlow;
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @ModelAttribute CreateBookRequestDTO dto, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Tạo sách thất bại");
		if (responseError != null)
			return responseError;
		Book book = createBookFlow.createBook(dto);
		var response = new ApiResponseDTO<Book>("Tạo sách thành công", true, book);
		return new ResponseEntity<ApiResponseDTO<Book>>(response, HttpStatus.OK);
	}

}

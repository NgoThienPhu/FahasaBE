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
import com.example.demo.book.service.BookService;
import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponseSuccessDTO;
import com.example.demo.util.validation.BindingResultUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/books")
public class AdminBookController {

	private BookService bookService;

	public AdminBookController(BookService bookService) {
		this.bookService = bookService;
	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @ModelAttribute CreateBookRequestDTO dto, HttpServletRequest request,
			BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Tạo sách thất bại",
				request.getRequestURI());
		if (responseError != null)
			return responseError;
		Book book = bookService.createBook(dto);
		var response = new ApiResponseSuccessDTO<Book>(201, "Tạo sách thành công", book);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

}

package com.example.demo.book.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponsePaginationSuccess;
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
	public ResponseEntity<ApiResponseDTO> getBookById(@PathVariable String bookId) {
		var book = bookService.getBookById(bookId);

		var response = new ApiResponseSuccessDTO<BookResponseDTO>(200, "Lấy sách thành công", book);

		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> createBook(@RequestBody @Valid CreateBookRequestDTO dto, HttpServletRequest request,
			BindingResult result) {

		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Tạo sách thất bại",
				request.getRequestURI());

		if (responseError != null)
			return responseError;

		BookResponseDTO book = bookService.createBook(dto);
		var response = new ApiResponseSuccessDTO<BookResponseDTO>(200, "Tạo sách thành công", book);

		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/{bookId}")
	public ResponseEntity<?> updateBook(@PathVariable String bookId, @RequestBody @Valid UpdateBookRequestDTO dto, HttpServletRequest request,
			BindingResult result) {

		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Cập nhật sách thất bại",
				request.getRequestURI());

		if (responseError != null)
			return responseError;

		BookResponseDTO book = bookService.updateBook(bookId, dto);
		var response = new ApiResponseSuccessDTO<BookResponseDTO>(200, "Cập nhật sách thành công", book);

		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

}

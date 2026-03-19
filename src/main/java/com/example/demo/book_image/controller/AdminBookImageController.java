package com.example.demo.book_image.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.book_image.entity.BookImage;
import com.example.demo.book_image.service.BookImageService;
import com.example.demo.util.response.ResponseFactory;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/books/{bookId}/images")
public class AdminBookImageController {

	private BookImageService bookImageService;
	private ResponseFactory responseFactory;

	public AdminBookImageController(BookImageService bookImageService, ResponseFactory responseFactory) {
		this.bookImageService = bookImageService;
		this.responseFactory = responseFactory;
	}

	@GetMapping("/primary")
	public ResponseEntity<?> getBookPrimaryImage(@PathVariable String bookId) {
		BookImage primaryImage = bookImageService.getBookPrimaryImage(bookId);
		return responseFactory.success(primaryImage, "Lấy ảnh chính thành công");
	}

	@GetMapping("/secondary")
	public ResponseEntity<?> getBookSecondaryImages(@PathVariable String bookId) {
		List<BookImage> secondaryImage = bookImageService.getBookSecondaryImages(bookId);
		return responseFactory.success(secondaryImage, "Lấy ảnh phụ thành công");
	}

	@PostMapping("/secondary")
	public ResponseEntity<?> addSecondaryImages(@PathVariable String bookId, @Valid @RequestBody List<MultipartFile> files) {
		List<BookImage> addedSecondaryImages = bookImageService.addBookSecondaryImages(bookId, files);
		return responseFactory.success(addedSecondaryImages, "Thêm ảnh phụ thành công",
				org.springframework.http.HttpStatus.CREATED);
	}

	@DeleteMapping("/secondary/{imageId}")
	public ResponseEntity<?> deleteSecondaryImage(@PathVariable String bookId, @PathVariable String imageId) {
		bookImageService.deleteBookSecondaryImage(bookId, imageId);
		return responseFactory.success("Xóa ảnh phụ thành công");
	}

	@PutMapping("/primary")
	public ResponseEntity<?> updatePrimaryImage(@PathVariable String bookId, @Valid @RequestBody MultipartFile file) {
		BookImage updatedPrimaryImage = bookImageService.updateBookPrimaryImage(bookId, file);
		return responseFactory.success(updatedPrimaryImage, "Cập nhật ảnh chính thành công",
				org.springframework.http.HttpStatus.CREATED);
	}

}
package com.example.demo.book_image.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.book_image.entity.BookImage;
import com.example.demo.book_image.service.BookImageService;
import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponseSuccessDTO;

@RestController
@RequestMapping("/api/admin/books/{bookId}/images")
public class AdminBookImageController {

	private BookImageService bookImageService;

	public AdminBookImageController(BookImageService bookImageService) {
		this.bookImageService = bookImageService;
	}

	@GetMapping("/primary")
	public ResponseEntity<?> getBookPrimaryImage(@PathVariable String bookId) {
		BookImage primaryImage = bookImageService.getBookPrimaryImage(bookId);
		var response = new ApiResponseSuccessDTO<BookImage>(200, "Lấy ảnh chính thành công", primaryImage);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@GetMapping("/secondary")
	public ResponseEntity<?> getBookSecondaryImages(@PathVariable String bookId) {
		List<BookImage> secondaryImage = bookImageService.getBookSecondaryImages(bookId);
		var response = new ApiResponseSuccessDTO<List<BookImage>>(200, "Lấy ảnh phụ thành công", secondaryImage);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/secondary", consumes = "multipart/form-data")
	public ResponseEntity<?> addBookSecondaryImages(@PathVariable String bookId,
			@RequestParam List<MultipartFile> files) {
		List<BookImage> addedSecondaryImages = bookImageService.addBookSecondaryImages(bookId, files);
		var response = new ApiResponseSuccessDTO<List<BookImage>>(200, "Thêm ảnh phụ thành công", addedSecondaryImages);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@DeleteMapping("/secondary/{imageId}")
	public ResponseEntity<?> deleteBookSecondaryImage(@PathVariable String bookId, @PathVariable String imageId) {
		bookImageService.deleteBookSecondaryImage(bookId, imageId);
		var response = new ApiResponseSuccessDTO<Void>(200, "Xóa ảnh phụ thành công");
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@PutMapping(value = "/primary", consumes = "multipart/form-data")
	public ResponseEntity<?> updateBookPrimaryImage(@PathVariable String bookId, @RequestParam MultipartFile file) {
		System.out.println("da vao");
		BookImage updatedPrimaryImage = bookImageService.updateBookPrimaryImage(bookId, file);
		var response = new ApiResponseSuccessDTO<BookImage>(200, "Cập nhật ảnh chính thành công", updatedPrimaryImage);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

}

package com.example.demo.book_image.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.book.entity.Book;
import com.example.demo.book.repository.BookRepository;
import com.example.demo.book_image.entity.BookImage;
import com.example.demo.book_image.repository.BookImageRepository;
import com.example.demo.book_image.service.BookImageService;
import com.example.demo.util.dto.upload_response.UploadResponseDTO;
import com.example.demo.util.exception.CustomException;
import com.example.demo.util.service.CloudinaryService;

import jakarta.transaction.Transactional;

@Service
public class BookImageService {

	private BookImageRepository bookImageRepository;
	private BookRepository bookRepository;
	private CloudinaryService cloudinaryService;

	public BookImageService(BookImageRepository bookImageRepository, BookRepository bookRepository,
			CloudinaryService cloudinaryService) {
		this.bookImageRepository = bookImageRepository;
		this.bookRepository = bookRepository;
		this.cloudinaryService = cloudinaryService;
	}

	public BookImage getBookPrimaryImage(String bookId) {
		return bookImageRepository.findBookPrimaryImage(bookId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy ảnh chính của sách"));
	}

	public List<BookImage> getBookSecondaryImages(String bookId) {
		return bookImageRepository.findBookSecondaryImage(bookId);
	}

	@Transactional(rollbackOn = Exception.class)
	public void deleteBookSecondaryImage(String bookId, String imageId) {
		BookImage secondaryImage = bookImageRepository.findById(imageId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy ảnh phụ của sách"));
		if (!secondaryImage.getBook().getId().equals(bookId)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Ảnh phụ không thuộc về sách");
		}
		cloudinaryService.deleteFile(secondaryImage.getPublicId());
		bookImageRepository.delete(secondaryImage);
	}

	@Transactional(rollbackOn = Exception.class)
	public List<BookImage> addBookSecondaryImages(String bookId, List<MultipartFile> files) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy sách"));
		
		List<BookImage> bookSecondaryImages = book.getBookImages().stream().filter(image -> !image.getIsPrimary()).toList();
		
		if(bookSecondaryImages.size() + files.size() > 5) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Số lượng ảnh phụ không được vượt quá 5");
		}

		return files.stream().map(file -> {
			UploadResponseDTO imageUpload = cloudinaryService.uploadFile(file);
			BookImage secondaryImage = new BookImage();
			secondaryImage.setPublicId(imageUpload.getPublicId());
			secondaryImage.setBook(book);
			secondaryImage.setUrl(imageUpload.getUrl());
			secondaryImage.setIsPrimary(false);
			return bookImageRepository.save(secondaryImage);
		}).toList();
	}

	@Transactional(rollbackOn = Exception.class)
	public BookImage updateBookPrimaryImage(String bookId, MultipartFile file) {
		UploadResponseDTO imageUpload = cloudinaryService.uploadFile(file);
		BookImage primaryImage = bookImageRepository.findBookPrimaryImage(bookId).orElse(null);
		if (primaryImage != null) {
			cloudinaryService.deleteFile(primaryImage.getPublicId());
			primaryImage.setPublicId(imageUpload.getPublicId());
			primaryImage.setUrl(imageUpload.getUrl());
			return bookImageRepository.save(primaryImage);
		} else {
			Book book = bookRepository.findById(bookId)
					.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy sách"));

			BookImage newPrimaryImage = new BookImage();
			newPrimaryImage.setPublicId(imageUpload.getPublicId());
			newPrimaryImage.setBook(book);
			newPrimaryImage.setUrl(imageUpload.getUrl());
			newPrimaryImage.setIsPrimary(true);
			return bookImageRepository.save(newPrimaryImage);
		}
	}

}

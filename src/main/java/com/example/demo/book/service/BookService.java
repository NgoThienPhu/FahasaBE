package com.example.demo.book.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.book.dto.BookResponseDTO;
import com.example.demo.book.dto.CreateBookRequestDTO;
import com.example.demo.book.dto.UpdateBookRequestDTO;
import com.example.demo.book.entity.Book;
import com.example.demo.book.repository.BookRepository;
import com.example.demo.book_price.entity.BasePrice;
import com.example.demo.book_price.entity.base.BookPrice;
import com.example.demo.book_price.repository.BasePriceRepository;
import com.example.demo.book_price.repository.BookPriceRepository;
import com.example.demo.category.entity.Category;
import com.example.demo.category.repository.CategoryRepository;
import com.example.demo.util.exception.CustomException;

@Service
public class BookService {

	private BookRepository bookRepository;
	private BookPriceRepository bookPriceRepository;
	private BasePriceRepository basePriceRepository;
	private CategoryRepository categoryRepository;

	public BookService(BookRepository bookRepository, BookPriceRepository bookPriceRepository,
			BasePriceRepository basePriceRepository, CategoryRepository categoryRepository) {
		this.bookRepository = bookRepository;
		this.bookPriceRepository = bookPriceRepository;
		this.basePriceRepository = basePriceRepository;
		this.categoryRepository = categoryRepository;
	}

	public Page<BookResponseDTO> getBooks(String search, String orderBy, String sortBy, int page, int size) {

		List<String> allowedFields = List.of("title", "publishDate", "author", "createdAt", "category");

		if (!allowedFields.contains(sortBy)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}

		Sort sort = orderBy.equalsIgnoreCase("asc")
				? Sort.by(Sort.Direction.ASC, sortBy.equals("category") ? "category.name" : sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy.equals("category") ? "category.name" : sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Book> bookPage = bookRepository.findBooks(search, pageable);
		List<Book> books = bookPage.getContent();
		if (books.isEmpty()) {
			return bookPage.map(book -> BookResponseDTO.fromEntity(book, book.getCategory(), null));
		}
		List<String> bookIds = books.stream().map(Book::getId).toList();
		List<BookPrice> prices = bookPriceRepository.findCurrentPrices(bookIds, LocalDateTime.now());
		Map<String, BookPrice> priceMap = prices.stream().collect(
				Collectors.toMap(price -> price.getBook().getId(), price -> price, (oldVal, newVal) -> oldVal));

		return bookPage.map(book -> BookResponseDTO.fromEntity(book, book.getCategory(), priceMap.get(book.getId())));
	}

	public BookResponseDTO getBookById(String bookId) {
		var book = bookRepository.findById(bookId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Sản phẩm không tồn tại"));

		var category = book.getCategory();
		BookPrice bookPrice = bookPriceRepository.findCurrentPrice(bookId, LocalDateTime.now())
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Giá sản phẩm không tồn tại"));

		return BookResponseDTO.fromEntity(book, category, bookPrice);
	}

	public BookResponseDTO createBook(CreateBookRequestDTO dto) {
		var category = findCategoryById(dto.getCategoryId());

		Book book = new Book();
		book.setTitle(dto.getTitle());
		book.setDescription(dto.getDescription());
		book.setAuthor(dto.getAuthor());
		book.setPublisher(dto.getPublisher());
		book.setIsbn(dto.getIsbn());
		book.setPublishDate(dto.getPublishDate());
		book.setCategory(category);

		book = bookRepository.save(book);

		BasePrice basePrice = new BasePrice(book, dto.getPrice(), LocalDateTime.now());
		basePrice = basePriceRepository.save(basePrice);
		book.getBookPrices().add(basePrice);

		return BookResponseDTO.fromEntity(book, category, basePrice);
	}

	public BookResponseDTO updateBook(String bookId, UpdateBookRequestDTO dto) {
		var book = bookRepository.findById(bookId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Sản phẩm không tồn tại"));

		var category = findCategoryById(dto.getCategoryId());
		book.setTitle(dto.getTitle());
		book.setDescription(dto.getDescription());
		book.setAuthor(dto.getAuthor());
		book.setPublisher(dto.getPublisher());
		book.setIsbn(dto.getIsbn());
		book.setPublishDate(dto.getPublishDate());
		book.setCategory(category);

		BookPrice bookPrice = bookPriceRepository.findCurrentPrice(bookId, LocalDateTime.now())
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Giá sản phẩm không tồn tại"));

		book = bookRepository.save(book);

		return BookResponseDTO.fromEntity(book, category, bookPrice);
	}

	private Category findCategoryById(String categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Loại sản phẩm không tồn tại"));
	}

}

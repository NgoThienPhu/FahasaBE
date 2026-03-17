package com.example.demo.book.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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
import com.example.demo.book_image.entity.BookImage;
import com.example.demo.book_image.repository.BookImageRepository;
import com.example.demo.book_price.entity.BasePrice;
import com.example.demo.book_price.entity.base.BookPrice;
import com.example.demo.book_price.repository.BasePriceRepository;
import com.example.demo.book_price.repository.BookPriceRepository;
import com.example.demo.category.entity.Category;
import com.example.demo.category.repository.CategoryRepository;
import com.example.demo.util.exception.CustomException;
import com.example.demo.util.service.CloudinaryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

	private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("title", "publishDate", "author", "createdAt",
			"category");

	private final BookRepository bookRepository;
	private final BookPriceRepository bookPriceRepository;
	private final BasePriceRepository basePriceRepository;
	private final CategoryRepository categoryRepository;
	private final BookImageRepository bookImageRepository;
	private final CloudinaryService cloudinaryService;

	public Page<BookResponseDTO> getBooks(String search, String orderBy, String sortBy, int page, int size) {

		if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}

		Pageable pageable = PageRequest.of(page, size, buildSort(orderBy, sortBy));
		Page<Book> bookPage = bookRepository.findBooks(search, pageable);

		List<Book> books = bookPage.getContent();
		List<String> bookIds = books.stream().map(Book::getId).toList();

		LocalDateTime now = LocalDateTime.now();

		Map<String, BookPrice> priceMap = bookPriceRepository.findCurrentPrices(bookIds, now).stream().collect(
				Collectors.toMap(price -> price.getBook().getId(), Function.identity(), (oldVal, newVal) -> oldVal));

		Map<String, BookImage> imageMap = bookImageRepository.findBookPrimaryImages(bookIds).stream()
				.collect(Collectors.toMap(img -> img.getBook().getId(), Function.identity()));

		return bookPage.map(book -> BookResponseDTO.fromEntity(book, imageMap.get(book.getId()), book.getCategory(),
				priceMap.get(book.getId())));
	}

	public BookResponseDTO getBookById(String bookId) {

		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Sản phẩm không tồn tại"));

		LocalDateTime now = LocalDateTime.now();

		BookPrice bookPrice = bookPriceRepository.findCurrentPrice(bookId, now)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Giá sản phẩm không tồn tại"));

		BookImage primaryImage = getPrimaryImage(book);

		return BookResponseDTO.fromEntity(book, primaryImage, book.getCategory(), bookPrice);
	}

	public List<BookResponseDTO> getBookByIds(List<String> bookIds) {

		var myBooks = new ArrayList<BookResponseDTO>();

		List<Book> books = bookRepository.findBookByIds(bookIds);

		var now = LocalDateTime.now();

		Map<String, BookPrice> priceMap = bookPriceRepository.findCurrentPrices(bookIds, now).stream().collect(
				Collectors.toMap(price -> price.getBook().getId(), Function.identity(), (oldVal, newVal) -> oldVal));

		Map<String, BookImage> imageMap = bookImageRepository.findBookPrimaryImages(bookIds).stream()
				.collect(Collectors.toMap(img -> img.getBook().getId(), Function.identity()));

		for (Book book : books) {
			myBooks.add(BookResponseDTO.fromEntity(book, imageMap.get(book.getId()), book.getCategory(),
					priceMap.get(book.getId())));
		}

		return myBooks;

	}

	@Transactional(rollbackOn = Exception.class)
	public BookResponseDTO createBook(CreateBookRequestDTO dto) {

		Category category = findCategoryById(dto.getCategoryId());

		Book book = new Book();
		book.setTitle(dto.getTitle());
		book.setSummary(dto.getSummary());
		book.setDescription(dto.getDescription());
		book.setAuthor(dto.getAuthor());
		book.setPublisher(dto.getPublisher());
		book.setIsbn(dto.getIsbn());
		book.setPublishDate(dto.getPublishDate());
		book.setCategory(category);

		book = bookRepository.save(book);

		LocalDateTime now = LocalDateTime.now();

		BasePrice basePrice = basePriceRepository.save(new BasePrice(book, dto.getPrice(), now));

		book.getBookPrices().add(basePrice);

		return BookResponseDTO.fromEntity(book, null, category, basePrice);
	}

	@Transactional(rollbackOn = Exception.class)
	public BookResponseDTO updateBook(String bookId, UpdateBookRequestDTO dto) {

		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Sản phẩm không tồn tại"));

		Category category = findCategoryById(dto.getCategoryId());

		book.setTitle(dto.getTitle());
		book.setDescription(dto.getDescription());
		book.setAuthor(dto.getAuthor());
		book.setPublisher(dto.getPublisher());
		book.setIsbn(dto.getIsbn());
		book.setPublishDate(dto.getPublishDate());
		book.setCategory(category);

		LocalDateTime now = LocalDateTime.now();

		BookPrice bookPrice = bookPriceRepository.findCurrentPrice(bookId, now)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Giá sản phẩm không tồn tại"));

		book = bookRepository.save(book);

		BookImage primaryImage = getPrimaryImage(book);

		return BookResponseDTO.fromEntity(book, primaryImage, category, bookPrice);
	}

	@Transactional(rollbackOn = Exception.class)
	public void deleteBook(String bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Sản phẩm không tồn tại"));

		List<BookImage> images = book.getBookImages();
		for (BookImage image : images) {
			cloudinaryService.deleteFile(image.getPublicId());
		}

		bookRepository.delete(book);
	}

	private Category findCategoryById(String categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Loại sản phẩm không tồn tại"));
	}

	private BookImage getPrimaryImage(Book book) {
		return book.getBookImages().stream().filter(BookImage::getIsPrimary).findFirst().orElse(null);
	}

	private Sort buildSort(String orderBy, String sortBy) {

		String field = sortBy.equals("category") ? "category.name" : sortBy;

		Sort.Direction direction = orderBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

		return Sort.by(direction, field);
	}

}
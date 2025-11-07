package com.example.demo.book.service;

import org.springframework.stereotype.Service;

import com.example.demo.book.entity.Book;
import com.example.demo.book.repository.BookRepository;

@Service
public class BookService {

	private BookRepository bookRepository;

	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public Book save(Book book) {
		return bookRepository.save(book);
	}

}

package com.example.demo.book.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.book.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

	@Query("""
			SELECT b
			FROM Book b
			WHERE :search IS NULL
			   OR LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))
			   OR LOWER(b.author) LIKE LOWER(CONCAT('%', :search, '%'))
			   OR LOWER(b.publisher) LIKE LOWER(CONCAT('%', :search, '%'))
			   OR LOWER(b.category.name) LIKE LOWER(CONCAT('%', :search, '%'))
			""")
	public Page<Book> findBooks(String search, Pageable pageable);

}

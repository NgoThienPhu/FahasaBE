package com.example.demo.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.book.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

}

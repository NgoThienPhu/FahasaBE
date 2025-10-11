package com.example.demo.book_image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.book_image.entity.BookImage;

@Repository
public interface ProductImageRepository extends JpaRepository<BookImage, String>, JpaSpecificationExecutor<BookImage> {

}

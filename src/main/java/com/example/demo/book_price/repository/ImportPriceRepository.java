package com.example.demo.book_price.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.book_price.entity.ImportPrice;

@Repository
public interface ImportPriceRepository extends JpaRepository<ImportPrice, String> {

}

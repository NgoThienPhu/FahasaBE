package com.example.demo.book_price.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.book_price.entity.SellPrice;

@Repository
public interface SellPriceRepository extends JpaRepository<SellPrice, String> {

}

package com.example.demo.book_price.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.book_price.entity.PromotionPrice;

@Repository
public interface PromotionPriceRepository extends JpaRepository<PromotionPrice, String> {

}

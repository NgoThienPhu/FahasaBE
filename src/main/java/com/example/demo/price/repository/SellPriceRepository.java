package com.example.demo.price.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.price.entity.SellPrice;

@Repository
public interface SellPriceRepository extends JpaRepository<SellPrice, String>, JpaSpecificationExecutor<SellPrice> {

}

package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.common.ProductPrice;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, String>, JpaSpecificationExecutor<ProductPrice> {

}

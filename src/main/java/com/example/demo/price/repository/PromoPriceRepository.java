package com.example.demo.price.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.price.entity.PromoPrice;

@Repository
public interface PromoPriceRepository extends JpaRepository<PromoPrice, String>, JpaSpecificationExecutor<PromoPrice> {

}

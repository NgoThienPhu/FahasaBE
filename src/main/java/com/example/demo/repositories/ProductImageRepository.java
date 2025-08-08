package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, String>, JpaSpecificationExecutor<ProductImage> {

}

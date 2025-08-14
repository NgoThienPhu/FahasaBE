package com.example.demo.productimage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.productimage.entity.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, String>, JpaSpecificationExecutor<ProductImage> {

}

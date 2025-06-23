package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
	
	public List<Product> findByCategoryId(String categoryId);
	
}

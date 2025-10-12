package com.example.demo.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.category.entity.Category;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
	
	@Query("""
			SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
			FROM Category c
			WHERE c.name = :categoryName
			""")
	boolean existsByCategoryName(@Param("categoryName") String categoryName);
	
}

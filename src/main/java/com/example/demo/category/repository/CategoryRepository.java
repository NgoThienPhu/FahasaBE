package com.example.demo.category.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.category.entity.Category;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

	@Query("""
			SELECT c
			FROM Category c
			WHERE (:search IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')))
			""")
	Page<Category> findAll(String search, Pageable pageable);

	@Query("""
			SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
			FROM Category c
			WHERE LOWER(c.name) = LOWER(:categoryName)
			""")
	boolean existsByCategoryName(@Param("categoryName") String categoryName);

	@Query("""
			SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
			FROM Book p
			WHERE p.category.id = :categoryId
			""")
	boolean isCategoryInUse(String categoryId);

}

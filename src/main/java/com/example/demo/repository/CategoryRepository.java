package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
	
	public Boolean existsByName(String categoryName);
	
	@Query("SELECT c FROM Category c WHERE c.name LIKE CONCAT('%', :categoryName, '%')")
	public List<Category> findByName(@Param("categoryName") String categoryName, Sort orderBy);
	
	Boolean existsByCategory_Id(String parentCategoryId);
	
}

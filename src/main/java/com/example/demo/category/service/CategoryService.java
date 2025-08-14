package com.example.demo.category.service;

import org.springframework.data.domain.Page;

import com.example.demo.category.dto.CreateCategoryRequestDTO;
import com.example.demo.category.dto.UpdateCategoryNameRequestDTO;
import com.example.demo.category.entity.Category;

public interface CategoryService {

	Page<Category> findAll(String categoryName, String orderBy, String sortBy, int page, int size);

	Category findById(String categoryId);

	Category create(CreateCategoryRequestDTO body);
	
	Category update(UpdateCategoryNameRequestDTO body, String categoryId);

	void deleteById(String categoryId);

	boolean existsByName(String categoryName);
	
	boolean hasChildren(String categoryId);

}

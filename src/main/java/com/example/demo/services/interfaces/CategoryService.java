package com.example.demo.services.interfaces;

import org.springframework.data.domain.Page;

import com.example.demo.dto.category.CreateCategoryRequestDTO;
import com.example.demo.dto.category.UpdateCategoryNameRequestDTO;
import com.example.demo.entities.Category;

public interface CategoryService {

	Page<Category> findAll(String categoryName, String orderBy, String sortBy, int page, int size);

	Category findById(String categoryId);

	Category create(CreateCategoryRequestDTO body);

	void deleteById(String categoryId);

	Category update(UpdateCategoryNameRequestDTO body, String categoryId);

	Boolean existsByName(String categoryName);

}

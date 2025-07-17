package com.example.demo.services.interfaces;

import org.springframework.data.domain.Page;

import com.example.demo.entities.Category;
import com.example.demo.validator.CategoryValidator;
import com.example.demo.validator.UpdateCategoryValidator;

public interface CategoryService {

	Page<Category> findAll(String categoryName, String orderBy, String sortBy, int page, int size);

	Category findById(String categoryId);

	Category create(CategoryValidator body);

	void deleteById(String categoryId);

	Category update(UpdateCategoryValidator body, String categoryId);

	Boolean existsByName(String categoryName);

}

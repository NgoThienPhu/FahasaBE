package com.example.demo.services.interfaces;

import java.util.List;

import com.example.demo.entities.Category;
import com.example.demo.validator.CategoryValidator;

public interface CategoryServiceInf {

	List<Category> getCategories(String categoryName, String sortBy);

	Category findCategoryById(String categoryId);

	Category createCategory(CategoryValidator body);

	void deleteCategory(String categoryId);

	Category updateCategory(CategoryValidator body, String categoryId);

}

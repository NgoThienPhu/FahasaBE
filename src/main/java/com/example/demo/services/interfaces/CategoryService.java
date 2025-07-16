package com.example.demo.services.interfaces;

import java.util.List;

import com.example.demo.entities.Category;
import com.example.demo.validator.CategoryValidator;
import com.example.demo.validator.UpdateCategoryValidator;

public interface CategoryService {

	List<Category> getCategories(String categoryName, String sortBy);

	Category findCategoryById(String categoryId);

	Category createCategory(CategoryValidator body);

	void deleteCategory(String categoryId);

	Category updateCategoryName(UpdateCategoryValidator body, String categoryId);

}

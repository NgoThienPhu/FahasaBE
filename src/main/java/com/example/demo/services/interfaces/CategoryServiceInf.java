package com.example.demo.services.interfaces;

import java.util.List;

import com.example.demo.entities.Category;
import com.example.demo.validator.CreateCategoryValidator;
import com.example.demo.validator.UpdateCategoryValidator;

public interface CategoryServiceInf {
	
	public List<Category> getCategories();
	
	public Category findCategoryById(String categoryId);
	
	public Category createCategory(CreateCategoryValidator body);
	
	public void deleteCategory(String categoryId);
	
	public Category updateCategory(UpdateCategoryValidator body);
	
}

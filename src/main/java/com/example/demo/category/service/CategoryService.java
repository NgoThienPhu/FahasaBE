package com.example.demo.category.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.category.dto.CreateCategoryRequestDTO;
import com.example.demo.category.dto.UpdateCategoryNameRequestDTO;
import com.example.demo.category.entity.Category;

public interface CategoryService {

	Page<Category> getAll(String orderBy, String sortBy, int page, int size);

	Page<Category> getAllByParentId(String parentId, String orderBy, String sortBy, int page, int size);

	List<Category> getLeafCategories();
	
	Category getTree();

	Category get(String categoryId);

	Category create(CreateCategoryRequestDTO body);

	Category update(UpdateCategoryNameRequestDTO body, String categoryId);

	void delete(String categoryId);

	boolean existsByName(String categoryName);

	boolean hasChildren(String categoryId);

}

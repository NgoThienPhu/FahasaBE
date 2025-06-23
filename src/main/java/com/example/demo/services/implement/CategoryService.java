package com.example.demo.services.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.services.interfaces.CategoryServiceInf;
import com.example.demo.validator.CategoryValidator;

import jakarta.transaction.Transactional;

@Service
public class CategoryService implements CategoryServiceInf {
	
	private CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	@Transactional
	@Override
	public Category createCategory(CategoryValidator body) {
		Boolean checkNameIsExists = categoryRepository.existsByName(body.name());
		if(checkNameIsExists) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên loại sản phẩm đã tồn tại vui lòng chọn tên khác");
		Category category = new Category();
		category.setName(body.name());
		return categoryRepository.save(category);
	}
	
	@Transactional
	@Override
	public void deleteCategory(String categoryId) {
		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
		if(optionalCategory.isPresent()) {
			Category category = optionalCategory.get();
			categoryRepository.delete(category);
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Không tìm thấy loại sản phẩm với id là: %s", categoryId));
		}
	}

	@Transactional
	@Override
	public Category updateCategory(CategoryValidator body, String categoryId) {
		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
		if(optionalCategory.isPresent()) {
			Boolean checkNameExists = categoryRepository.existsByName(body.name());
			if(checkNameExists) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên loại sản phẩm đã tồn tại, vui lòng chọn tên khác");	
			Category category = optionalCategory.get();
			category.setName(body.name());
			return categoryRepository.save(category);
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Không tìm thấy loại sản phẩm với id là: %s", categoryId));
		}
	}

	@Override
	public Category findCategoryById(String categoryId) {
		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
		if(optionalCategory.isPresent()) {
			Category category = optionalCategory.get();
			return category;
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Không tìm thấy loại sản phẩm với id là: %s", categoryId));
		}
	}

	@Override
	public List<Category> getCategories(String categoryName, String orderBy) {
		Sort myOrderBy = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, "name")
				: Sort.by(Sort.Direction.DESC, "name");
		if(categoryName == null || categoryName.isEmpty()) {
			return categoryRepository.findAll(myOrderBy);
		}else {
			return categoryRepository.findByName(categoryName, myOrderBy);
		}
	}

}

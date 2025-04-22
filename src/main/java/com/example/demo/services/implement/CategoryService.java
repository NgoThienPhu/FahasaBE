package com.example.demo.services.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.services.interfaces.CategoryServiceInf;
import com.example.demo.validator.CreateCategoryValidator;
import com.example.demo.validator.UpdateCategoryValidator;

import jakarta.transaction.Transactional;

@Service
public class CategoryService implements CategoryServiceInf {
	
	private CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	@Transactional
	@Override
	public Category createCategory(CreateCategoryValidator body) {
		Boolean checkNameIsExists = categoryRepository.existsByName(body.getName());
		if(checkNameIsExists) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên loại sản phẩm đã tồn tại vui lòng chọn tên khác");
		Category category = new Category();
		category.setName(body.getName());
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
	public Category updateCategory(UpdateCategoryValidator body) {
		Optional<Category> optionalCategory = categoryRepository.findById(body.getId());
		if(optionalCategory.isPresent()) {
			Boolean checkNameExists = categoryRepository.existsByName(body.getName());
			if(checkNameExists) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên loại sản phẩm đã tồn tại, vui lòng chọn tên khác");	
			Category category = optionalCategory.get();
			category.setName(body.getName());
			return categoryRepository.save(category);
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Không tìm thấy loại sản phẩm với id là: %s", body.getId()));
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
	public List<Category> getCategories() {
		return categoryRepository.findAll();
	}

}

package com.example.demo.category.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.category.dto.CreateCategoryRequestDTO;
import com.example.demo.category.dto.UpdateCategoryNameRequestDTO;
import com.example.demo.category.entity.Category;
import com.example.demo.category.repository.CategoryRepository;
import com.example.demo.category.service.CategoryService;
import com.example.demo.category.specification.CategorySpecification;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {

	private CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public Category findById(String categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("Không tìm thấy loại sản phẩm với Id là: %s", categoryId)));
	}

	public Page<Category> findAll(String orderBy, String sortBy, int page, int size) {
		List<String> allowedFields = List.of("name");

		if (!allowedFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}

		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);

		return categoryRepository.findAll(pageable);
	}

	@Transactional
	public Category create(CreateCategoryRequestDTO dto) {

		boolean existsName = existsByName(dto.categoryName());

		if (existsName)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Tên loại sản phẩm đã tồn tại vui lòng chọn tên khác");

		Category parentCategory = findById(dto.parentCategoryId());

		if (parentCategory == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					String.format("Không tìm thấy loại sản phẩm có Id là: %s", dto.parentCategoryId()));

		Category category = new Category(dto.categoryName());

		return categoryRepository.save(category);

	}

	@Transactional
	public Category update(UpdateCategoryNameRequestDTO dto, String categoryId) {
		Category category = findById(categoryId);

		if (category == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					String.format("Không tìm thấy loại sản phẩm với Id là: %s", categoryId));

		if (dto.categoryName() != null)
			category.setName(dto.categoryName());

		return categoryRepository.save(category);
	}

	@Transactional
	public void deleteById(String categoryId) {
		Category category = findById(categoryId);

		if (category == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					String.format("Không tìm thấy loại sản phẩm có Id là: %s", categoryId));

		categoryRepository.delete(category);
	}

	public boolean existsByName(String categoryName) {
		Specification<Category> spec = CategorySpecification.hasName(categoryName);
		return categoryRepository.count(spec) > 0;
	}

}

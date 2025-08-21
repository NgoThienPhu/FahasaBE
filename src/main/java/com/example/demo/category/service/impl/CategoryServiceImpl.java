package com.example.demo.category.service.impl;

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
public class CategoryServiceImpl implements CategoryService {

	private CategoryRepository categoryRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Category get(String categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("Không tìm thấy loại sản phẩm với Id là: %s", categoryId)));
	}

	@Override
	public Page<Category> getAll(String orderBy, String sortBy, int page, int size) {
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

	@Override
	public Page<Category> getAllByParentId(String parentId, String orderBy, String sortBy, int page, int size) {

		List<String> allowedFields = List.of("name");

		if (!allowedFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}

		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);
		Specification<Category> spec = CategorySpecification.hasParent(parentId);

		get(parentId);

		return categoryRepository.findAll(spec, pageable);
	}

	@Override
	public List<Category> getLeafCategories() {
		return categoryRepository.findAll(CategorySpecification.hasNoChildren());
	}

	@Override
	public Category getTree() {
		return categoryRepository.findOne(CategorySpecification.hasNoParent()).orElseGet(() -> {
			Category category = new Category("Danh sách thể loại");
			return categoryRepository.save(category);
		});
	}

	@Transactional
	@Override
	public Category create(CreateCategoryRequestDTO dto) {

		boolean existsName = existsByName(dto.categoryName());

		if (existsName)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Tên loại sản phẩm đã tồn tại vui lòng chọn tên khác");

		Category parentCategory = get(dto.parentCategoryId());

		if (parentCategory == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					String.format("Không tìm thấy loại sản phẩm có Id là: %s", dto.parentCategoryId()));

		Category category = new Category(dto.categoryName(), parentCategory);

		return categoryRepository.save(category);

	}

	@Transactional
	@Override
	public Category update(UpdateCategoryNameRequestDTO body, String categoryId) {
		Category category = get(categoryId);

		if (category == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					String.format("Không tìm thấy loại sản phẩm với Id là: %s", categoryId));

		category.setName(body.categoryName());

		return categoryRepository.save(category);
	}

	@Transactional
	@Override
	public void delete(String categoryId) {
		Category category = get(categoryId);

		if (category == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					String.format("Không tìm thấy loại sản phẩm có Id là: %s", categoryId));

		boolean hasChild = hasChildren(categoryId);

		if (hasChild) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Vui lòng xóa tất cả loại sản phẩm con trước khi xóa loại sản phẩm này");
		} else {
			categoryRepository.delete(category);
		}
	}

	@Override
	public boolean existsByName(String categoryName) {
		Specification<Category> spec = CategorySpecification.hasName(categoryName);
		return categoryRepository.count(spec) > 0;
	}

	@Override
	public boolean hasChildren(String categoryId) {
		Specification<Category> spec = Specification.where(CategorySpecification.hasId(categoryId))
				.and(CategorySpecification.hasChildren());
		return categoryRepository.count(spec) > 0;
	}

}

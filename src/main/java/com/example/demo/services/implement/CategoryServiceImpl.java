	package com.example.demo.services.implement;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.services.interfaces.CategoryService;
import com.example.demo.services.interfaces.ProductService;
import com.example.demo.specification.CategorySpecification;
import com.example.demo.validator.CategoryValidator;
import com.example.demo.validator.UpdateCategoryValidator;

import jakarta.transaction.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

	private CategoryRepository categoryRepository;

	private ProductService productService;

	public CategoryServiceImpl(CategoryRepository categoryRepository, @Lazy ProductService productService) {
		this.categoryRepository = categoryRepository;
		this.productService = productService;
	}

	@Transactional
	@Override
	public Category create(CategoryValidator body) {

		Specification<Category> spec = CategorySpecification.nameEquals(body.categoryName());
		Boolean nameExists = categoryRepository.count(spec) > 0;

		if (nameExists)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Tên loại sản phẩm đã tồn tại vui lòng chọn tên khác");

		if (body.parentCategoryId() != null) {

			Category parentCategory = categoryRepository.findById(body.parentCategoryId())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
							String.format("Không tìm thấy loại sản phẩm có Id là: %s", body.parentCategoryId())));

			Category category = new Category(body.categoryName(), parentCategory);

			return categoryRepository.save(category);
		}

		Category category = new Category(body.categoryName());

		return categoryRepository.save(category);
	}

	@Transactional
	@Override
	public void deleteById(String categoryId) {
		Category category = categoryRepository.findById(categoryId).orElse(null);
		if (category == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					String.format("Không tìm thấy loại sản phẩm có Id là: %s", categoryId));
		
		Specification<Category> spec = CategorySpecification.hasChildren(categoryId);
		Boolean hasChildren = categoryRepository.count(spec) > 0;
		if (hasChildren) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(
					"Không thể xóa loại sản phẩm với Id là: %s vì vẫn còn danh mục con. Vui lòng xóa tất cả danh mục con trước.",
					categoryId));
		} else {
			Boolean hasProducts = productService.existsProductsByCategoryId(categoryId);
			if (hasProducts)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(
						"Không thể xóa loại sản phẩm với Id là: %s vì vẫn còn sản phẩm sử dụng loại sản phẩm này",
						categoryId));
			categoryRepository.delete(category);
		}
	}

	@Transactional
	@Override
	public Category update(UpdateCategoryValidator body, String categoryId) {
		Category category = categoryRepository.findById(categoryId).orElse(null);
		if (category == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					String.format("Không tìm thấy loại sản phẩm với Id là: %s", categoryId));
		category.setName(body.categoryName());
		return categoryRepository.save(category);
	}

	@Override
	public Category findById(String categoryId) {
		return categoryRepository.findById(categoryId).orElse(null);
	}

	@Override
	public Page<Category> findAll(String categoryName, String orderBy, String sortBy, int page, int size) {

		List<String> allowedFields = List.of("name");
		if (!allowedFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}

		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);

		if ((categoryName == null || categoryName.isEmpty())) {
			return categoryRepository.findAll(pageable);
		} else {
			Specification<Category> spec = CategorySpecification.hasName(categoryName);
			return categoryRepository.findAll(spec, pageable);
		}
	}

	@Override
	public Boolean existsByName(String categoryName) {
		Specification<Category> spec = CategorySpecification.hasName(categoryName);
		return categoryRepository.count(spec) > 0;
	}

}

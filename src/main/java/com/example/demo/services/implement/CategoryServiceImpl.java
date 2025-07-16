package com.example.demo.services.implement;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.services.interfaces.CategoryService;
import com.example.demo.services.interfaces.ProductService;
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
	public Category createCategory(CategoryValidator body) {

		Boolean nameExists = categoryRepository.existsByName(body.categoryName());

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
	public void deleteCategory(String categoryId) {
		Category category = categoryRepository.findById(categoryId).orElse(null);
		if (category == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					String.format("Không tìm thấy loại sản phẩm có Id là: %s", categoryId));
		Boolean hasChildren = categoryRepository.existsByCategory_Id(categoryId);
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
	public Category updateCategoryName(UpdateCategoryValidator body, String categoryId) {
		Category category = categoryRepository.findById(categoryId).orElse(null);
		if (category == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					String.format("Không tìm thấy loại sản phẩm với Id là: %s", categoryId));
		category.setName(body.categoryName());
		return categoryRepository.save(category);
	}

	@Override
	public Category findCategoryById(String categoryId) {
		return categoryRepository.findById(categoryId).orElse(null);
	}

	@Override
	public List<Category> getCategories(String categoryName, String orderBy) {
		Sort myOrderBy = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, "name")
				: Sort.by(Sort.Direction.DESC, "name");
		return (categoryName == null || categoryName.isEmpty()) ? categoryRepository.findAll(myOrderBy)
				: categoryRepository.findByName(categoryName, myOrderBy);
	}

}

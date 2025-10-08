package com.example.demo.product.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.category.entity.Category;
import com.example.demo.category.service.CategoryService;
import com.example.demo.product.dto.UpdateProductRequestDTO;
import com.example.demo.product.entity.Product;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.product.service.ProductService;
import com.example.demo.productimage.entity.ProductImage;
import com.example.demo.util.service.S3Service;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;
	private CategoryService categoryService;
	private S3Service s3Service;

	public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService,
			S3Service s3Service) {
		this.productRepository = productRepository;
		this.categoryService = categoryService;
		this.s3Service = s3Service;
	}

	@Override
	public Page<Product> findAll(String orderBy, String sortBy, int page, int size) {
		List<String> allowedFields = List.of("name", "price");

		if (!allowedFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thuộc tính cần sắp xếp không hợp lệ, vui lòng thử lại");
		}

		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);
		return productRepository.findAll(pageable);
	}

	@Transactional
	@Override
	public void deleteById(String productId) {
		Product product = findById(productId);
		productRepository.delete(product);
	}

	@Override
	public Product findById(String productId) {
		return productRepository.findById(productId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				String.format("Không tìm thấy sản phẩm với Id: %s", productId)));
	}

	@Override
	public Product update(String productId, String productName, String description, Category category) {
		Product product = findById(productId);

		if (category == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Không tìm thấy loại sản phẩm, vui lòng thử lại sau");
		}

		product.setCategory(category);

		if (productName != null)
			product.setName(productName);
		if (description != null)
			product.setDescription(description);

		return productRepository.save(product);
	}

	@Override
	public Product update(String productId, UpdateProductRequestDTO dto) {
		Category category = dto.categoryId() != null ? categoryService.get(dto.categoryId()) : null;
		return update(productId, dto.productName(), dto.description(), category);
	}

	@Override
	public Product updatePrimaryImage(String productId, MultipartFile primaryImage) {
		String newPrimaryImageUrl = null;

		try {
			Product product = findById(productId);

			for (ProductImage img : product.getImages()) {
				if (img.getIsPrimary()) {
					img.setIsPrimary(false);
					break;
				}
			}

			newPrimaryImageUrl = s3Service.uploadFile(primaryImage);
			product.getImages().add(new ProductImage(newPrimaryImageUrl, true));

			return productRepository.save(product);

		} catch (Exception e) {
			if (newPrimaryImageUrl != null) {
				s3Service.deleteFile(S3Service.convertFileURlToFileName(newPrimaryImageUrl));
			}
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Có lỗi xảy ra khi cập nhật ảnh chính của sản phẩm", e);
		}
	}

	@Override
	public Product updateSecondImage(String productId, List<MultipartFile> secondImages) {
		List<String> imageUrls = new ArrayList<>();

		try {
			Product product = findById(productId);

			List<String> secondImageUrls = new ArrayList<>();
			for (MultipartFile file : secondImages) {
				secondImageUrls.add(s3Service.uploadFile(file));
			}

			imageUrls.addAll(secondImageUrls);

			product.getImages().addAll(secondImageUrls.stream().map(url -> new ProductImage(url, false)).toList());

			return productRepository.save(product);

		} catch (Exception e) {
			imageUrls.forEach(url -> s3Service.deleteFile(S3Service.convertFileURlToFileName(url)));
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Có lỗi xảy ra khi cập nhật ảnh phụ của sản phẩm", e);
		}
	}

	@Override
	public Product deleteSecondImage(String productId, List<String> secondImageIds) {
		Product product = findById(productId);

		List<ProductImage> secondImages = product.getImages().stream().filter(img -> !img.getIsPrimary())
				.filter(img -> secondImageIds.contains(img.getId())).toList();

		if (secondImages.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ảnh phụ cần xóa");
		}

		for (ProductImage img : secondImages) {
			try {
				s3Service.deleteFile(S3Service.convertFileURlToFileName(img.getUrl()));
			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						String.format("Không thể xóa ảnh trên S3: %s", img.getUrl()), e);
			}
		}

		product.getImages().removeAll(secondImages);
		return productRepository.save(product);

	}

	@Override
	public Product save(Product product) {
		return productRepository.save(product);
	}
}
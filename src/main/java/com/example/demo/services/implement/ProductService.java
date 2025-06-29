package com.example.demo.services.implement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ProductRequestDTO;
import com.example.demo.entities.Category;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductImage;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.interfaces.CategoryServiceInf;
import com.example.demo.services.interfaces.ProductServiceInf;
import com.example.demo.validator.CategoryValidator;

@Service
public class ProductService implements ProductServiceInf {

	private ProductRepository productRepository;

	private CategoryServiceInf categoryService;

	private S3Service s3Service;

	public ProductService(ProductRepository productRepository, CategoryServiceInf categoryService,
			S3Service s3Service) {
		this.productRepository = productRepository;
		this.categoryService = categoryService;
		this.s3Service = s3Service;
	}

	@Override
	public List<Product> findProductsByCateogryId(String categoryId) {
		return productRepository.findByCategoryId(categoryId);
	}

	@Override
	public Product createProduct(ProductRequestDTO product, MultipartFile mainImage, List<MultipartFile> images)
			throws IOException {

		List<String> uploadedImageUrls = new ArrayList<>();

		try {
			String productMainImageURL = s3Service.uploadFile(mainImage);
			uploadedImageUrls.add(productMainImageURL);

			List<String> productImagesURL = s3Service.uploadFiles(images);
			uploadedImageUrls.addAll(productImagesURL);

			ProductImage productMainImage = new ProductImage(productMainImageURL, true);
			List<ProductImage> productImages = new ArrayList<>();
			productImages.add(productMainImage);

			for (String productImageURL : productImagesURL) {
				productImages.add(new ProductImage(productImageURL, false));
			}

			Category category = (product.category().categoryId() != null)
					? categoryService.findCategoryById(product.category().categoryId())
					: categoryService.createCategory(new CategoryValidator(product.category().categoryName()));

			Product myProduct = new Product(product.name(), product.description(), category, product.price(),
					product.quantity(), productImages);
			for (ProductImage productImage : productImages) {
				productImage.setProduct(myProduct);
			}

			return productRepository.save(myProduct);
		} catch (Exception e) {
			for (String imageUrl : uploadedImageUrls) {
				try {
					s3Service.deleteFile(ProductImage.extractFileNameFromUrl(imageUrl));
				} catch (Exception ignore) {
					throw ignore;
				}
			}
			throw e;
		}
	}

	@Override
	public Product updateProduct() {
		return null;
	}

	@Override
	public void deleteById(String productId) {

	}

}

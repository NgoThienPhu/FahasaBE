package com.example.demo.services.implement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.ProductAttributeValueDTO;
import com.example.demo.dto.ProductRequestDTO;
import com.example.demo.entities.Attribute;
import com.example.demo.entities.Category;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductAttributeValue;
import com.example.demo.entities.ProductImage;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.interfaces.AttributeService;
import com.example.demo.services.interfaces.CategoryService;
import com.example.demo.services.interfaces.ProductService;
import com.example.demo.validator.AttributeValidator;

@Service
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;

	private CategoryService categoryService;

	private AttributeService attributeServie;

	private S3ServiceImpl s3ServiceImpl;

	public ProductServiceImpl(ProductRepository productRepository, @Lazy CategoryService categoryService,
			AttributeService attributeServie, S3ServiceImpl s3ServiceImpl) {
		this.productRepository = productRepository;
		this.categoryService = categoryService;
		this.attributeServie = attributeServie;
		this.s3ServiceImpl = s3ServiceImpl;
	}

	@Override
	public Product createProduct(ProductRequestDTO productDTO, MultipartFile mainImage, List<MultipartFile> images)
			throws IOException {

		List<String> uploadedImageUrls = new ArrayList<>();
		List<ProductAttributeValue> attributesValue = new ArrayList<>();

		try {
			attributesValue = handleAttributeValues(productDTO.attributes());

			String mainImageUrl = s3ServiceImpl.uploadFile(mainImage);
			uploadedImageUrls.add(mainImageUrl);

			List<String> imageUrls = s3ServiceImpl.uploadFiles(images);
			uploadedImageUrls.addAll(imageUrls);

			List<ProductImage> productImages = handleProductImages(mainImageUrl, imageUrls);

			Category category = handleCategory(productDTO.categoryId());

			Product product = new Product(productDTO.name(), productDTO.description(), category, productDTO.price(),
					productDTO.quantity(), productImages, attributesValue);

			productImages.forEach(img -> img.setProduct(product));
			attributesValue.forEach(attr -> attr.setProduct(product));

			return productRepository.save(product);

		} catch (Exception e) {
			cleanupUploadedFiles(uploadedImageUrls);
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

	@Override
	public Product findById(String productId) {
		return productRepository.findById(productId).orElse(null);
	}

	private List<ProductAttributeValue> handleAttributeValues(List<ProductAttributeValueDTO> attributeDTOs) {
		List<ProductAttributeValue> result = new ArrayList<>();
		if (attributeDTOs != null) {
			for (ProductAttributeValueDTO dto : attributeDTOs) {
				Attribute attr = (dto.attributeId() != null) ? attributeServie.findById(dto.attributeId())
						: attributeServie.createAttribute(new AttributeValidator(dto.attributeName()));

				result.add(new ProductAttributeValue(attr, dto.attributeValue()));
			}
		}
		return result;
	}

	private List<ProductImage> handleProductImages(String mainImageUrl, List<String> imageUrls) {
		List<ProductImage> images = new ArrayList<>();
		images.add(new ProductImage(mainImageUrl, true));
		for (String url : imageUrls) {
			images.add(new ProductImage(url, false));
		}
		return images;
	}

	private Category handleCategory(String categoryId) {
		Category category = categoryService.findCategoryById(categoryId);
		if (category == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					String.format("Không tìm thấy loại sản phẩm với id là: %s", categoryId));
		return category;
	}

	private void cleanupUploadedFiles(List<String> uploadedImageUrls) {
		for (String url : uploadedImageUrls) {
			try {
				s3ServiceImpl.deleteFile(ProductImage.extractFileNameFromUrl(url));
			} catch (Exception ex) {
				// Ghi log...
			}
		}
	}

	@Override
	public Boolean existsProductsByCategoryId(String categoryId) {
		return productRepository.existsByCategory_Id(categoryId);
	}

}

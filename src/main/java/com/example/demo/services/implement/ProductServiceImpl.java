package com.example.demo.services.implement;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.CreateAttributeRequestDTO;
import com.example.demo.dto.CreateAttributeValueRequestDTO;
import com.example.demo.dto.ProductFilterDTO;
import com.example.demo.dto.UpdateProductRequestDTO;
import com.example.demo.dto.CreateProductRequestDTO;
import com.example.demo.entities.Attribute;
import com.example.demo.entities.Category;
import com.example.demo.entities.Product;
import com.example.demo.entities.AttributeValue;
import com.example.demo.entities.ProductImage;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.interfaces.AttributeService;
import com.example.demo.services.interfaces.CategoryService;
import com.example.demo.services.interfaces.ProductService;
import com.example.demo.services.interfaces.S3Service;
import com.example.demo.specification.ProductSpecification;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;

	private CategoryService categoryService;

	private AttributeService attributeServie;

	private S3Service s3Service;

	public ProductServiceImpl(ProductRepository productRepository, @Lazy CategoryService categoryService,
			AttributeService attributeServie, S3Service s3Service) {
		this.productRepository = productRepository;
		this.categoryService = categoryService;
		this.attributeServie = attributeServie;
		this.s3Service = s3Service;
	}

	@Override
	public Page<Product> findAll(ProductFilterDTO dto, String orderBy, String sortBy, int page, int size) {
		List<String> allowedFields = List.of("name", "price");
		if (!allowedFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}

		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);

		if (dto != null) {
			Specification<Product> spec = buildProductSpecification(dto);
			return productRepository.findAll(spec, pageable);
		} else {
			return productRepository.findAll(pageable);
		}
	}

	@Transactional
	@Override
	public Product createProduct(CreateProductRequestDTO productDTO, MultipartFile mainImage,
			List<MultipartFile> images) throws IOException {
		List<String> uploadedImageUrls = new ArrayList<>();
		List<AttributeValue> attributesValue = new ArrayList<>();
		try {
			if (mainImage == null)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng chọn ảnh đại diện cho sản phẩm");

			attributesValue = handleAttributeValues(productDTO.attributes());

			String mainImageUrl = s3Service.uploadFile(mainImage);
			uploadedImageUrls.add(mainImageUrl);

			List<String> imageUrls = s3Service.uploadFiles(images);
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

	@Transactional
	@Override
	public Product createProduct(CreateProductRequestDTO dto, MultipartFile mainImage) throws IOException {
		List<String> uploadedImageUrls = new ArrayList<>();
		List<AttributeValue> attributesValue = new ArrayList<>();

		try {

			if (mainImage == null)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng chọn ảnh đại diện cho sản phẩm");

			attributesValue = handleAttributeValues(dto.attributes());

			String mainImageUrl = s3Service.uploadFile(mainImage);
			uploadedImageUrls.add(mainImageUrl);

			List<ProductImage> productImages = List.of(new ProductImage(mainImageUrl, true));

			Category category = handleCategory(dto.categoryId());

			Product product = new Product(dto.name(), dto.description(), category, dto.price(), dto.quantity(),
					productImages, attributesValue);

			productImages.forEach(img -> img.setProduct(product));
			attributesValue.forEach(attr -> attr.setProduct(product));

			return productRepository.save(product);

		} catch (Exception e) {
			cleanupUploadedFiles(uploadedImageUrls);
			throw e;
		}
	}

	@Transactional
	@Override
	public Product updateProduct(String productId, UpdateProductRequestDTO dto) {
		Product product = productRepository.findById(productId).orElse(null);

		if (product == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Không tìm thấy sản phẩm với id là: %s", productId));

		handleUpdateProductRequest(product, dto);

		return productRepository.save(product);
	}

	@Transactional
	@Override
	public void deleteById(String productId) {
		Product product = productRepository.findById(productId).orElse(null);
		if (product == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Không tìm thấy sản phẩm với id là: %s", productId));

		ListIterator<ProductImage> productImages = product.getImages().listIterator();
		while (productImages.hasNext()) {
			ProductImage productImage = productImages.next();
			s3Service.deleteFile(ProductImage.extractFileNameFromUrl(productImage.getUrl()));
			productImages.remove();
		}

		productRepository.deleteById(productId);
	}

	@Override
	public Product findById(String productId) {
		return productRepository.findById(productId).orElse(null);
	}

	@Override
	public Boolean existsProductsByCategoryId(String categoryId) {
		Specification<Product> spec = ProductSpecification.hasCategoryId(categoryId);
		return productRepository.count(spec) > 0;
	}

	@Transactional
	@Override
	public Product updateNewMainImage(String productId, MultipartFile newMainImage) throws Exception {
		String mainImage = null;
		try {
			Product product = productRepository.findById(productId).orElse(null);
			if (product == null)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						String.format("Không tìm thấy sản phẩm với Id là: %s", productId));

			mainImage = s3Service.uploadFile(newMainImage);
			ProductImage productImage = new ProductImage(mainImage, true);
			productImage.setProduct(product);

			product.getImages().stream().filter(img -> Boolean.TRUE.equals(img.getIsPrimary())).findFirst()
					.ifPresent(img -> img.setIsPrimary(false));

			product.getImages().add(productImage);

			return productRepository.save(product);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			if (mainImage != null)
				s3Service.deleteFile(ProductImage.extractFileNameFromUrl(mainImage));
			throw e;
		}
	}

	@Transactional
	@Override
	public Product updateImages(String productId, List<MultipartFile> images) {
		List<String> uploadedImageUrls = new ArrayList<>();
		try {
			Product product = productRepository.findById(productId).orElse(null);
			if (product == null)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						String.format("Không tìm thấy sản phẩm với Id là: %s", productId));

			List<String> imageUrls = s3Service.uploadFiles(images);
			uploadedImageUrls.addAll(imageUrls);

			List<ProductImage> productImages = imageUrls.stream()
					.map(imageUrl -> new ProductImage(imageUrl, false, product)).collect(Collectors.toList());

			product.getImages().addAll(productImages);

			return productRepository.save(product);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			cleanupUploadedFiles(uploadedImageUrls);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Đã có lỗi xảy ra trong quá trình cập nhật ảnh của sản phẩm, vui lòng thử lại sau");
		}
	}

	@Transactional
	@Override
	public Product deleteImages(String productId, List<String> imagesId) {
		try {
			Product product = productRepository.findById(productId).orElse(null);
			if (product == null)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						String.format("Không tìm thấy sản phẩm với Id là: %s", productId));

			Optional<ProductImage> mainImage = product.getImages().stream()
					.filter(image -> image.getIsPrimary() == true).findFirst();

			if (mainImage.isPresent() && imagesId.contains(mainImage.get().getId())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						String.format("Không thể xóa ảnh chính của sản phẩm với Id là: %s", mainImage.get().getId()));
			}

			product.getImages().stream().filter(image -> imagesId.contains(image.getId())).map(ProductImage::getUrl)
					.map(ProductImage::extractFileNameFromUrl).forEach(s3Service::deleteFile);

			product.getImages().removeIf(image -> imagesId.contains(image.getId()));

			return productRepository.save(product);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	private List<AttributeValue> handleAttributeValues(
			List<CreateAttributeValueRequestDTO> attributeDTOs) {
		List<AttributeValue> result = new ArrayList<>();
		if (attributeDTOs != null) {
			for (CreateAttributeValueRequestDTO dto : attributeDTOs) {
				Attribute attr = (dto.attributeId() != null) ? attributeServie.findById(dto.attributeId())
						: attributeServie.createAttribute(new CreateAttributeRequestDTO(dto.attributeName()));

				result.add(new AttributeValue(attr, dto.attributeValue()));
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
		Category category = categoryService.findById(categoryId);
		if (category == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					String.format("Không tìm thấy loại sản phẩm với id là: %s", categoryId));
		return category;
	}

	private void cleanupUploadedFiles(List<String> uploadedImageUrls) {
		for (String url : uploadedImageUrls) {
			try {
				s3Service.deleteFile(ProductImage.extractFileNameFromUrl(url));
			} catch (Exception ex) {
				// Ghi log...
			}
		}
	}

	private Specification<Product> buildProductSpecification(ProductFilterDTO dto) {
		Specification<Product> spec = Specification.where(null);

		if (dto.productName() != null && !dto.productName().isBlank()) {
			spec = spec.and(ProductSpecification.hasName(dto.productName()));
		}

		if (dto.categoryId() != null && !dto.categoryId().isBlank()) {
			spec = spec.and(ProductSpecification.hasCategoryId(dto.categoryId()));
		}

		if (dto.minPrice() != null || dto.maxPrice() != null) {
			spec = spec.and(ProductSpecification.priceBetween(dto.minPrice(), dto.maxPrice()));
		}

		if (dto.attributes() != null && !dto.attributes().isEmpty()) {
			for (Map.Entry<String, String> entry : dto.attributes().entrySet()) {
				spec = spec.and(ProductSpecification.hasAttribute(entry.getKey(), entry.getValue()));
			}
		}

		return spec;
	}

	private Product handleUpdateProductRequest(Product product, UpdateProductRequestDTO dto) {
		if (dto.productName() != null)
			product.setName(dto.productName());

		if (dto.description() != null)
			product.setDescription(dto.description());

		if (dto.categoryId() != null) {
			Category category = categoryService.findById(dto.categoryId());
			product.setCategory(category);
		}

		if (dto.price() != null) {
			if (dto.price().compareTo(BigDecimal.valueOf(1000)) > 0)
				product.setPrice(dto.price());
			else
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giá của sản phẩm phải lớn hơn 1_000 VNĐ");
		}

		if (dto.quantity() != null) {
			if (dto.quantity() > 0)
				product.setQuantity(dto.quantity());
			else
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số lượng sản phẩm phải lớn hơn 1");
		}

		return product;
	}

}

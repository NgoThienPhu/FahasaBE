package com.example.demo.services.implement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
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

import com.example.demo.dto.attribute.AttributeResponseDTO;
import com.example.demo.dto.attribute.CreateAttributeRequestDTO;
import com.example.demo.dto.attribute.CreateAttributeValueRequestDTO;
import com.example.demo.dto.price.ProductPriceResponseDTO;
import com.example.demo.dto.price.PromoPriceResponseDTO;
import com.example.demo.dto.product.CreateProductRequestDTO;
import com.example.demo.dto.product.ProductFilterDTO;
import com.example.demo.dto.product.ProductResponseDTO;
import com.example.demo.dto.product.UpdateProductRequestDTO;
import com.example.demo.entities.Attribute;
import com.example.demo.entities.Category;
import com.example.demo.entities.Product;
import com.example.demo.entities.AttributeValue;
import com.example.demo.entities.ProductImage;
import com.example.demo.entities.price.PromoPrice;
import com.example.demo.entities.price.PurchasePrice;
import com.example.demo.entities.price.SellPrice;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.interfaces.AttributeService;
import com.example.demo.services.interfaces.CategoryService;
import com.example.demo.services.interfaces.ProductPriceService;
import com.example.demo.services.interfaces.ProductService;
import com.example.demo.services.interfaces.S3Service;
import com.example.demo.specifications.ProductSpecification;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;

	private CategoryService categoryService;

	private AttributeService attributeServie;

	private S3Service s3Service;

	private ProductPriceService productPriceService;

	public ProductServiceImpl(ProductRepository productRepository, @Lazy CategoryService categoryService,
			AttributeService attributeServie, S3Service s3Service, ProductPriceService productPriceService) {
		this.productRepository = productRepository;
		this.categoryService = categoryService;
		this.attributeServie = attributeServie;
		this.s3Service = s3Service;
		this.productPriceService = productPriceService;
	}

	@Transactional
	@Override
	public Product save(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Page<ProductResponseDTO> findAll(ProductFilterDTO dto, String orderBy, String sortBy, int page, int size) {
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
			return productRepository.findAll(spec, pageable).map(this::convertProductToProductResponseDTO);
		} else {
			return productRepository.findAll(pageable).map(this::convertProductToProductResponseDTO);
		}
	}

	@Transactional
	@Override
	public ProductResponseDTO createProduct(CreateProductRequestDTO productDTO, MultipartFile mainImage,
			List<MultipartFile> images) throws IOException {
		List<String> uploadedImageUrls = new ArrayList<>();
		List<AttributeValue> attributesValue = new ArrayList<>();
		try {

			attributesValue = handleAttributeValues(productDTO.attributes());

			List<ProductImage> productImages = new ArrayList<>();

			if (mainImage != null) {
				String mainImageUrl = s3Service.uploadFile(mainImage);
				productImages.add(new ProductImage(mainImageUrl, true));
				uploadedImageUrls.add(mainImageUrl);
			}

			if (images != null) {
				List<String> imageUrls = s3Service.uploadFiles(images);
				List<ProductImage> bookImages = imageUrls.stream().map(url -> new ProductImage(url, false))
						.collect(Collectors.toList());
				productImages.addAll(bookImages);
				uploadedImageUrls.addAll(imageUrls);
			}

			Category category = handleCategory(productDTO.categoryId());

			Product product = new Product(productDTO.name(), productDTO.description(), category, productImages,
					attributesValue);

			product = productRepository.save(product);

			SellPrice sellPrice = new SellPrice(product, productDTO.sellPrice());

			PurchasePrice purchasePrice = new PurchasePrice(product,
					productDTO.purchasePrice() == null ? productDTO.sellPrice() : productDTO.purchasePrice());

			productPriceService.save(purchasePrice);
			productPriceService.save(sellPrice);

			return convertProductToProductResponseDTO(product);

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
	public ProductResponseDTO getProductResponseById(String productId) {
		Product product = productRepository.findById(productId).orElse(null);
		return convertProductToProductResponseDTO(product);
	}

	@Override
	public Product getProductEntityById(String productId) {
		return productRepository.findById(productId).orElse(null);
	}

	@Override
	public Boolean existsProductsByCategoryId(String categoryId) {
		Specification<Product> spec = ProductSpecification.hasCategoryId(categoryId);
		return productRepository.count(spec) > 0;
	}

	private List<AttributeValue> handleAttributeValues(List<CreateAttributeValueRequestDTO> attributeDTOs) {
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

	private Category handleCategory(String categoryId) {
		Category category = categoryService.findById(categoryId);
		if (category == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					String.format("Không tìm thấy loại sản phẩm với id là: %s", categoryId));
		return category;
	}

	private void cleanupUploadedFiles(List<String> uploadedImageUrls) {
		for (String url : uploadedImageUrls) {
			s3Service.deleteFile(ProductImage.extractFileNameFromUrl(url));
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

		return product;
	}

	private ProductResponseDTO convertProductToProductResponseDTO(Product product) {

		if (product == null)
			return null;

		SellPrice sellPrice = productPriceService.getProductCurrentSellPrice(product.getId());

		PromoPrice promoPrice = productPriceService.getProductCurrentPromoPrice(product.getId());

		ProductPriceResponseDTO productPrice = new ProductPriceResponseDTO(sellPrice,
				(promoPrice != null) ? PromoPriceResponseDTO.fromEntity(promoPrice) : null);

		List<AttributeResponseDTO> attributes = product.getAttributeValues().stream()
				.map(attributeValue -> new AttributeResponseDTO(attributeValue.getAttribute().getName(),
						attributeValue.getValue()))
				.collect(Collectors.toList());

		ProductResponseDTO productResponseDTO = new ProductResponseDTO(product.getId(), product.getName(),
				product.getDescription(), product.getCategory().getName(), productPrice, product.getImages(),
				attributes);

		return productResponseDTO;
	}

}

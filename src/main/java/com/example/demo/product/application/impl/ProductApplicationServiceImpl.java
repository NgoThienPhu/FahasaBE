package com.example.demo.product.application.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.attribute.dto.AttributeResponseDTO;
import com.example.demo.attributevalue.entity.AttributeValue;
import com.example.demo.attributevalue.service.AttributeValueService;
import com.example.demo.category.entity.Category;
import com.example.demo.category.service.CategoryService;
import com.example.demo.common.service.S3Service;
import com.example.demo.price.dto.CreatePromoPriceRequestDTO;
import com.example.demo.price.dto.ProductPriceResponseDTO;
import com.example.demo.price.dto.PromoPriceResponseDTO;
import com.example.demo.price.entity.PromoPrice;
import com.example.demo.price.entity.PurchasePrice;
import com.example.demo.price.entity.SellPrice;
import com.example.demo.price.service.PromoPriceService;
import com.example.demo.price.service.PurchasePriceService;
import com.example.demo.price.service.SellPriceService;
import com.example.demo.product.application.ProductApplicationService;
import com.example.demo.product.dto.CreateProductRequestDTO;
import com.example.demo.product.dto.ProductResponseDTO;
import com.example.demo.product.dto.UpdateProductRequestDTO;
import com.example.demo.product.entity.Product;
import com.example.demo.product.service.ProductService;
import com.example.demo.productimage.entity.ProductImage;

@Service
public class ProductApplicationServiceImpl implements ProductApplicationService {

	private ProductService productService;

	private SellPriceService sellPriceService;

	private PromoPriceService promoPriceService;

	private PurchasePriceService purchasePriceService;

	private CategoryService categoryService;

	private AttributeValueService attributeValueService;

	private S3Service s3Service;

	public ProductApplicationServiceImpl(ProductService productService, SellPriceService sellPriceService,
			PromoPriceService promoPriceService, PurchasePriceService purchasePriceService,
			CategoryService categoryService, AttributeValueService attributeValueService, S3Service s3Service) {
		this.productService = productService;
		this.sellPriceService = sellPriceService;
		this.promoPriceService = promoPriceService;
		this.purchasePriceService = purchasePriceService;
		this.categoryService = categoryService;
		this.attributeValueService = attributeValueService;
		this.s3Service = s3Service;
	}

	@Override
	public Product findById(String productId) {
		return productService.findById(productId);
	}

	@Override
	public ProductResponseDTO getProductResponseDTOById(String productId) {
		Product product = productService.findById(productId);
		return convertProductToProductResponseDTO(product);
	}

	@Override
	public Page<ProductResponseDTO> getPageProductResponseDTO(String orderBy, String sortBy, int page, int size) {
		Page<Product> pageProduct = productService.findAll(orderBy, sortBy, page, size);
		return pageProduct.map(this::convertProductToProductResponseDTO);
	}

	@Override
	public void deleteById(String productId) {
		productService.deleteById(productId);
	}

	@Override
	public ProductResponseDTO create(CreateProductRequestDTO dto, MultipartFile primaryImage,
			List<MultipartFile> secondImages) {
		List<String> imageUrls = new ArrayList<String>();
		try {
			Category category = categoryService.findById(dto.categoryId());

			Product product = new Product();
			product.setName(dto.name());
			product.setDescription(dto.description());
			product.setCategory(category);

			String primaryImageUrl = s3Service.uploadFile(primaryImage);
			imageUrls.add(primaryImageUrl);

			product.getImages().add(new ProductImage(primaryImageUrl, true));

			if (!secondImages.isEmpty()) {
				List<String> secondImageUrls = new ArrayList<String>();
				for (MultipartFile file : secondImages) {
					secondImageUrls.add(s3Service.uploadFile(file));
				}
				imageUrls.addAll(secondImageUrls);

				product.getImages()
						.addAll(secondImageUrls.stream().map(image -> new ProductImage(image, false)).toList());
			}

			List<AttributeValue> attributeValues = dto.attributeValues().stream().map(attributeValueService::findById)
					.toList();

			product.setAttributeValues(attributeValues);

			Product createProduct = productService.save(product);

			return convertProductToProductResponseDTO(createProduct);
		} catch (IOException e) {
			if (imageUrls != null) {
				for (String imageUrl : imageUrls) {
					String fileName = S3Service.convertFileURlToFileName(imageUrl);
					s3Service.deleteFile(fileName);
				}
			}
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi upload ảnh sản phẩm", e);
		}
	}

	@Override
	public ProductResponseDTO update(String productId, UpdateProductRequestDTO dto) {
		Category category = null;
		if (dto.categoryId() != null)
			category = categoryService.findById(dto.categoryId());
		Product product = productService.update(productId, dto.productName(), dto.description(), category);

		return convertProductToProductResponseDTO(product);
	}

	@Override
	public ProductResponseDTO updatePrimaryImage(String productId, MultipartFile primaryImage) {
		String newPrimaryImageUrl = null;
		try {
			Product product = productService.findById(productId);
			Iterator<ProductImage> iterator = product.getImages().iterator();
			while (iterator.hasNext()) {
				ProductImage img = iterator.next();
				if (img.getIsPrimary()) {
					img.setIsPrimary(false);
					break;
				}
			}

			newPrimaryImageUrl = s3Service.uploadFile(primaryImage);

			product.getImages().add(new ProductImage(newPrimaryImageUrl, true));

			Product savedProduct = productService.save(product);

			return convertProductToProductResponseDTO(savedProduct);

		} catch (Exception e) {
			if (newPrimaryImageUrl != null)
				s3Service.deleteFile(S3Service.convertFileURlToFileName(newPrimaryImageUrl));
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Có lỗi xảy ra khi cập nhật ảnh chính của sản phẩm", e);
		}
	}

	@Override
	public ProductResponseDTO updateSecondImage(String productId, List<MultipartFile> secondImages) {
		List<String> imageUrls = new ArrayList<>();
		try {
			Product product = productService.findById(productId);

			List<String> secondImageUrls = new ArrayList<String>();
			for (MultipartFile file : secondImages) {
				secondImageUrls.add(s3Service.uploadFile(file));
			}
			imageUrls.addAll(secondImageUrls);
			product.getImages().addAll(secondImageUrls.stream().map(image -> new ProductImage(image, false)).toList());

			Product savedProduct = productService.save(product);

			return convertProductToProductResponseDTO(savedProduct);

		} catch (Exception e) {
			if (imageUrls != null) {
				for (String imageUrl : imageUrls) {
					String fileName = S3Service.convertFileURlToFileName(imageUrl);
					s3Service.deleteFile(fileName);
				}
			}
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Có lỗi xảy ra khi cập nhật ảnh phụ của sản phẩm", e);
		}
	}

	public ProductResponseDTO deleteSecondImage(String productId, List<String> secondImageIds) {
		Product product = productService.findById(productId);
		List<ProductImage> secondImages = product.getImages().stream().filter(img -> !img.getIsPrimary())
				.filter(img -> secondImageIds.contains(img.getId())).toList();

		if (secondImages.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ảnh phụ cần xóa");
		}

		for (ProductImage img : secondImages) {
			try {
				String fileName = S3Service.convertFileURlToFileName(img.getUrl());
				s3Service.deleteFile(fileName);
			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						String.format("Không thể xóa ảnh trên S3: %s", img.getUrl()), e);
			}
		}
		product.getImages().removeAll(secondImages);

		productService.save(product);

		return convertProductToProductResponseDTO(product);
	}

	@Override
	public Page<SellPrice> findAllSellPrice(String productId, String orderBy, String sortBy, int page, int size) {
		return sellPriceService.findAll(productId, sortBy, orderBy, page, size);
	}

	@Override
	public SellPrice createSellPrice(String productId, BigDecimal newSellPrice) {
		return sellPriceService.updateProductSellPrice(productId, newSellPrice);
	}

	@Override
	public Page<PurchasePrice> findAllPurchasePrice(String productId, String orderBy, String sortBy, int page,
			int size) {
		return purchasePriceService.findAll(productId, sortBy, orderBy, page, size);
	}

	@Override
	public PurchasePrice createPurchasePrice(String productId, BigDecimal newPurchasePrice) {
		return purchasePriceService.updatePurchasePrice(productId, newPurchasePrice);
	}

	@Override
	public Page<PromoPrice> findAllPromoPrice(String productId, String orderBy, String sortBy, int page, int size) {
		return promoPriceService.findAll(productId, sortBy, orderBy, page, size);
	}
	
	@Override
	public PromoPrice createPromoPrice(Product product, CreatePromoPriceRequestDTO dto) {
		return promoPriceService.create(product, dto);
	}

	private ProductResponseDTO convertProductToProductResponseDTO(Product product) {

		PromoPrice promoPrice = promoPriceService.getCurrentPromoPrice(product.getId());

		SellPrice sellPrice = sellPriceService.getCurrentSellPrice(product.getId());

		PromoPriceResponseDTO promoPriceResponseDTO = PromoPriceResponseDTO.fromEntity(promoPrice);

		ProductPriceResponseDTO productPriceResponseDTO = new ProductPriceResponseDTO(sellPrice, promoPriceResponseDTO);

		List<AttributeResponseDTO> attributeResponseDTOs = product.getAttributeValues().stream()
				.map(AttributeResponseDTO::fromEntity).toList();

		return new ProductResponseDTO(product.getId(), product.getName(), product.getDescription(),
				product.getCategory().getName(), productPriceResponseDTO, product.getImages(), attributeResponseDTOs);

	}

}

package com.example.demo.product.flow;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.attributevalue.entity.AttributeValue;
import com.example.demo.attributevalue.service.AttributeValueService;
import com.example.demo.category.entity.Category;
import com.example.demo.category.service.CategoryService;
import com.example.demo.price.entity.PurchasePrice;
import com.example.demo.price.entity.SellPrice;
import com.example.demo.price.service.PurchasePriceService;
import com.example.demo.price.service.SellPriceService;
import com.example.demo.product.dto.CreateProductRequestDTO;
import com.example.demo.product.entity.Product;
import com.example.demo.product.service.ProductService;
import com.example.demo.productimage.entity.ProductImage;
import com.example.demo.util.service.S3Service;

@Service
public class CreateProductFlow {

	private ProductService productService;

	private CategoryService categoryService;

	private S3Service s3Service;

	private AttributeValueService attributeValueService;

	private SellPriceService sellPriceService;

	private PurchasePriceService purchasePriceService;

	public Product create(CreateProductRequestDTO dto, MultipartFile primaryImage, List<MultipartFile> secondImages) {
		List<String> imageUrls = new ArrayList<>();

		try {
			Category category = categoryService.get(dto.categoryId());

			Product product = new Product();
			product.setName(dto.name());
			product.setDescription(dto.description());
			product.setCategory(category);

			String primaryImageUrl = s3Service.uploadFile(primaryImage);
			imageUrls.add(primaryImageUrl);
			product.getImages().add(new ProductImage(primaryImageUrl, true));

			if (secondImages != null) {
				List<String> secondImageUrls = new ArrayList<>();
				for (MultipartFile file : secondImages) {
					secondImageUrls.add(s3Service.uploadFile(file));
				}
				imageUrls.addAll(secondImageUrls);

				product.getImages().addAll(secondImageUrls.stream().map(url -> new ProductImage(url, false)).toList());
			}

			if (dto.attributeValues() != null) {
				List<AttributeValue> attributeValues = dto.attributeValues().stream()
						.map(attributeValueService::findById).toList();
				product.setAttributeValues(attributeValues);
			}

			Product createdProduct = productService.save(product);

			sellPriceService.save(new SellPrice(createdProduct, dto.sellPrice()));

			BigDecimal purchasePrice = (dto.purchasePrice() == null) ? dto.sellPrice() : dto.purchasePrice();

			purchasePriceService.save(new PurchasePrice(createdProduct, purchasePrice));

			return createdProduct;

		} catch (IOException e) {
			rollbackImages(imageUrls);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi upload ảnh sản phẩm", e);
		} catch (ResponseStatusException e) {
			rollbackImages(imageUrls);
			throw e;
		} catch (Exception e) {
			rollbackImages(imageUrls);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Lỗi khi tạo sản phẩm, vui lòng thử lại sau", e);
		}
	}

	private void rollbackImages(List<String> imageUrls) {
		if (imageUrls != null) {
			imageUrls.stream().map(S3Service::convertFileURlToFileName).forEach(s3Service::deleteFile);
		}
	}

}

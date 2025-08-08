package com.example.demo.services.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.Product;
import com.example.demo.entities.ProductImage;
import com.example.demo.repositories.ProductImageRepository;
import com.example.demo.services.interfaces.ProductImageService;
import com.example.demo.services.interfaces.ProductService;
import com.example.demo.services.interfaces.S3Service;
import com.example.demo.specifications.ProductImageSpecification;

import jakarta.transaction.Transactional;

@Service
public class ProductImageServiceImpl implements ProductImageService {
	
	private ProductImageRepository productImageRepository;
	
	private ProductService productService;
	
	private S3Service s3Service;
	
	public ProductImageServiceImpl(ProductImageRepository productImageRepository, ProductService productService,
			S3Service s3Service) {
		this.productImageRepository = productImageRepository;
		this.productService = productService;
		this.s3Service = s3Service;
	}

	@Transactional
	@Override
	public void updateProductMainImage(String productId, MultipartFile newMainImage) throws Exception {
		String mainImage = null;
		try {
			Product product = productService.getProductEntityById(productId);
			if (product == null)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						String.format("Không tìm thấy sản phẩm với Id là: %s", productId));

			mainImage = s3Service.uploadFile(newMainImage);
			ProductImage productImage = new ProductImage(mainImage, true);

			product.getImages().stream().filter(img -> Boolean.TRUE.equals(img.getIsPrimary())).findFirst()
					.ifPresent(img -> img.setIsPrimary(false));

			product.getImages().add(productImage);

			productService.save(product);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			s3Service.deleteFile(ProductImage.extractFileNameFromUrl(mainImage));
			throw e;
		}
	}

	@Transactional
	@Override
	public void updateProductImages(String productId, List<MultipartFile> images) {
		List<String> uploadedImageUrls = new ArrayList<>();
		try {
			Product product = productService.getProductEntityById(productId);
			if (product == null)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						String.format("Không tìm thấy sản phẩm với Id là: %s", productId));

			List<String> imageUrls = s3Service.uploadFiles(images);
			uploadedImageUrls.addAll(imageUrls);

			List<ProductImage> productImages = imageUrls.stream().map(imageUrl -> new ProductImage(imageUrl, false))
					.collect(Collectors.toList());

			product.getImages().addAll(productImages);

			productService.save(product);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			s3Service.cleanupUploadedFiles(uploadedImageUrls);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Đã có lỗi xảy ra trong quá trình cập nhật ảnh của sản phẩm, vui lòng thử lại sau");
		}
	}

	@Transactional
	@Override
	public void deleteProductImages(String productId, List<String> imagesId) {
		try {
			Specification<ProductImage> spec = Specification.where(ProductImageSpecification.hasProduct(productId))
					.and(ProductImageSpecification.hasSecondaryImage());
			
			List<ProductImage> images = productImageRepository.findAll(spec);
			
			for (ProductImage productImage : images) {
				if(imagesId.contains(productImage.getId()))
					productImageRepository.deleteById(productImage.getId());
			}
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	

}

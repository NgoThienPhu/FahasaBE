package com.example.demo.services.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ProductImageService {
	
	void updateProductMainImage(String productId, MultipartFile newMainImage) throws Exception;
	
	void updateProductImages(String productId, List<MultipartFile> images);
	
	void deleteProductImages(String productId, List<String> imagesUrl);
	
}

package com.example.demo.product.application;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.product.dto.CreateProductRequestDTO;
import com.example.demo.product.dto.ProductResponseDTO;
import com.example.demo.product.dto.UpdateProductRequestDTO;

public interface ProductApplicationService {
	
	ProductResponseDTO getProductResponseDTOById(String productId);

	Page<ProductResponseDTO> getPageProductResponseDTO(String orderBy, String sortBy, int page, int size);
	
	void deleteById(String productId);
	
	ProductResponseDTO create(CreateProductRequestDTO dto, MultipartFile primaryImage, List<MultipartFile> secondImages);
	
	ProductResponseDTO update(String productId, UpdateProductRequestDTO dto);
	
	ProductResponseDTO updatePrimaryImage(String productId, MultipartFile primaryImage);
	
	ProductResponseDTO updateSecondImage(String productId, List<MultipartFile> secondImages);
	
	ProductResponseDTO deleteSecondImage(String productId, List<String> secondImageIds);
	
}

package com.example.demo.product.application;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.price.dto.CreatePromoPriceRequestDTO;
import com.example.demo.price.entity.PromoPrice;
import com.example.demo.price.entity.PurchasePrice;
import com.example.demo.price.entity.SellPrice;
import com.example.demo.product.dto.CreateProductRequestDTO;
import com.example.demo.product.dto.ProductResponseDTO;
import com.example.demo.product.dto.UpdateProductRequestDTO;
import com.example.demo.product.entity.Product;

public interface ProductApplicationService {
	
	Product findById(String productId);
	
	ProductResponseDTO getProductResponseDTOById(String productId);

	Page<ProductResponseDTO> getPageProductResponseDTO(String orderBy, String sortBy, int page, int size);
	
	void deleteById(String productId);
	
	ProductResponseDTO create(CreateProductRequestDTO dto, MultipartFile primaryImage, List<MultipartFile> secondImages);
	
	ProductResponseDTO update(String productId, UpdateProductRequestDTO dto);
	
	ProductResponseDTO updatePrimaryImage(String productId, MultipartFile primaryImage);
	
	ProductResponseDTO updateSecondImage(String productId, List<MultipartFile> secondImages);
	
	ProductResponseDTO deleteSecondImage(String productId, List<String> secondImageIds);
	
	Page<SellPrice> findAllSellPrice(String productId, String orderBy, String sortBy, int page, int size);
	
	SellPrice createSellPrice(String productId, BigDecimal newSellPrice);
	
	Page<PurchasePrice> findAllPurchasePrice(String productId, String orderBy, String sortBy, int page, int size);
	
	PurchasePrice createPurchasePrice(String productId, BigDecimal newPurchasePrice);
	
	Page<PromoPrice> findAllPromoPrice(String productId, String orderBy, String sortBy, int page, int size);
	
	PromoPrice createPromoPrice(Product product, CreatePromoPriceRequestDTO dto);
	
}

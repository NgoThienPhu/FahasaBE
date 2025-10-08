package com.example.demo.product.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.attribute.dto.AttributeResponseDTO;
import com.example.demo.price.dto.ProductPriceResponseDTO;
import com.example.demo.price.dto.PromoPriceResponseDTO;
import com.example.demo.price.entity.PromoPrice;
import com.example.demo.price.entity.SellPrice;
import com.example.demo.price.service.PromoPriceService;
import com.example.demo.price.service.SellPriceService;
import com.example.demo.product.dto.ProductDetailDTO;
import com.example.demo.product.entity.Product;

@Component
public class ProductDetailMapper {
	
	private PromoPriceService promoPriceService;
	
	private SellPriceService sellPriceService;
	
	public ProductDetailMapper(PromoPriceService promoPriceService, SellPriceService sellPriceService) {
		this.promoPriceService = promoPriceService;
		this.sellPriceService = sellPriceService;
	}

	public ProductDetailDTO convertToProductDetailDTO(Product product) {
		PromoPrice promoPrice = promoPriceService.getCurrentPromoPrice(product.getId());
		SellPrice sellPrice = sellPriceService.getCurrentSellPrice(product.getId());

		PromoPriceResponseDTO promoPriceResponseDTO = PromoPriceResponseDTO.fromEntity(promoPrice);
		ProductPriceResponseDTO productPriceResponseDTO = new ProductPriceResponseDTO(sellPrice, promoPriceResponseDTO);

		List<AttributeResponseDTO> attributeResponseDTOs = product.getAttributeValues().stream()
				.map(AttributeResponseDTO::fromEntity).toList();

		return new ProductDetailDTO(product.getId(), product.getName(), product.getDescription(),
				product.getCategory().getName(), productPriceResponseDTO, product.getImages(), attributeResponseDTOs);
	}

}

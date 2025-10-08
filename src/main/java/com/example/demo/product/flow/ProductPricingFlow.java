package com.example.demo.product.flow;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.price.dto.CreatePromoPriceRequestDTO;
import com.example.demo.price.entity.PromoPrice;
import com.example.demo.price.entity.SellPrice;
import com.example.demo.price.service.PromoPriceService;
import com.example.demo.price.service.SellPriceService;
import com.example.demo.product.entity.Product;
import com.example.demo.product.service.ProductService;

@Service
public class ProductPricingFlow {

	private ProductService productService;

	private PromoPriceService promoPriceService;
	
	private SellPriceService sellPriceService;
	
	public ProductPricingFlow(ProductService productService, PromoPriceService promoPriceService,
			SellPriceService sellPriceService) {
		this.productService = productService;
		this.promoPriceService = promoPriceService;
		this.sellPriceService = sellPriceService;
	}

	public SellPrice createSellPrice(String productId, BigDecimal newSellPrice) {
		Product myProduct = productService.findById(productId);
		SellPrice sellPrice = new SellPrice(myProduct, newSellPrice);
		sellPrice.setProduct(myProduct);
		return sellPriceService.save(sellPrice);
	}

	public PromoPrice createPromoPrice(String productId, CreatePromoPriceRequestDTO dto) {

		if (dto.endDate().isBefore(dto.startDate())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày kết thúc phải sau ngày bắt đầu");
		}

		Boolean checkOverlap = promoPriceService.existsOverlap(productId, dto.startDate(), dto.endDate());

		if (checkOverlap)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Đã có khuyến mại được thiết lập trong khoảng thời gian này");

		Product product = productService.findById(productId);

		PromoPrice promoPrice = new PromoPrice(product, dto.promoName(), dto.promoPrice(), dto.startDate(),
				dto.endDate());

		return promoPriceService.save(promoPrice);
	}

}

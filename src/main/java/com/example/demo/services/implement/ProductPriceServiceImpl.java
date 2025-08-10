package com.example.demo.services.implement;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.price.CreatePromoPriceRequestDTO;
import com.example.demo.entities.Product;
import com.example.demo.entities.common.ProductPrice;
import com.example.demo.entities.price.PromoPrice;
import com.example.demo.entities.price.PurchasePrice;
import com.example.demo.entities.price.SellPrice;
import com.example.demo.repositories.ProductPriceRepository;
import com.example.demo.repositories.PromoPriceRepository;
import com.example.demo.repositories.PurchasePriceRepository;
import com.example.demo.repositories.SellPriceRepository;
import com.example.demo.services.interfaces.ProductPriceService;
import com.example.demo.services.interfaces.ProductService;
import com.example.demo.specifications.PromoPriceSpecification;
import com.example.demo.specifications.PurchasePriceSpecification;
import com.example.demo.specifications.SellPriceSpecification;

@Service
public class ProductPriceServiceImpl implements ProductPriceService {

	private ProductPriceRepository productPriceRepository;

	private PromoPriceRepository promoPriceRepository;

	private SellPriceRepository sellPriceRepository;

	private PurchasePriceRepository purchasePriceRepository;

	private ProductService productService;

	public ProductPriceServiceImpl(ProductPriceRepository productPriceRepository,
			PromoPriceRepository promoPriceRepository, SellPriceRepository sellPriceRepository,
			PurchasePriceRepository purchasePriceRepository, @Lazy ProductService productService) {
		this.productPriceRepository = productPriceRepository;
		this.promoPriceRepository = promoPriceRepository;
		this.sellPriceRepository = sellPriceRepository;
		this.purchasePriceRepository = purchasePriceRepository;
		this.productService = productService;
	}

	@Transactional
	@Override
	public ProductPrice save(ProductPrice productPrice) {
		return productPriceRepository.save(productPrice);
	}

	@Override
	public SellPrice getProductCurrentSellPrice(String productId) {

		Specification<SellPrice> fallbackSpec = Specification.where(SellPriceSpecification.hasProductId(productId))
				.and(SellPriceSpecification.isActive());

		return sellPriceRepository.findOne(fallbackSpec).orElse(null);
	}

	@Override
	public PromoPrice getProductCurrentPromoPrice(String productId) {
		Specification<PromoPrice> spec = Specification.where(PromoPriceSpecification.hasProductId(productId))
				.and(PromoPriceSpecification.isActive());

		List<PromoPrice> promoPrices = promoPriceRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "startDate"));

		return promoPrices.stream().findFirst().orElse(null);
	}

	@Override
	public PurchasePrice getProductCurrentPurchasePrice(String productId) {
		Specification<PurchasePrice> spec = Specification.where(PurchasePriceSpecification.hasProductId(productId))
				.and(PurchasePriceSpecification.isActive());

		return purchasePriceRepository.findOne(spec).orElse(null);
	}

	@Override
	public Boolean existsOverlappingPromotion(String productId, LocalDateTime startDate, LocalDateTime endDate) {
		Specification<PromoPrice> spec = Specification.where(PromoPriceSpecification.hasProductId(productId))
				.and(PromoPriceSpecification.hasOverlap(startDate, endDate));

		return promoPriceRepository.count(spec) > 0;
	}

	@Transactional
	@Override
	public SellPrice updateProductSellPrice(String productId, BigDecimal newPrice) {

		if (newPrice.compareTo(BigDecimal.valueOf(1_000)) < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giá của sản phẩm phải lớn hơn 1.000 VNĐ");
		}

		SellPrice currentSellPrice = getProductCurrentSellPrice(productId);

		if (currentSellPrice != null) {
			currentSellPrice.setEndDate(LocalDateTime.now());
			productPriceRepository.save(currentSellPrice);
		}

		SellPrice newSellPrice = new SellPrice(currentSellPrice.getProduct(), newPrice);

		return sellPriceRepository.save(newSellPrice);
	}

	@Transactional
	@Override
	public PurchasePrice updateProductPurchasePrice(String productId, BigDecimal newPrice) {

		if (newPrice.compareTo(BigDecimal.valueOf(1_000)) < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giá của sản phẩm phải lớn hơn 1.000 VNĐ");
		}

		PurchasePrice currentPurchasePrice = getProductCurrentPurchasePrice(productId);

		if (currentPurchasePrice != null) {
			currentPurchasePrice.setEndDate(LocalDateTime.now());
			productPriceRepository.save(currentPurchasePrice);
		}

		PurchasePrice newSellPrice = new PurchasePrice(currentPurchasePrice.getProduct(), newPrice);

		return purchasePriceRepository.save(newSellPrice);
	}

	@Transactional
	@Override
	public PromoPrice createProductPromoPrice(String productId, CreatePromoPriceRequestDTO dto) {

		if (dto.endDate().isBefore(dto.startDate())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày kết thúc phải sau ngày bắt đầu");
		}

		Boolean checkOverlap = existsOverlappingPromotion(productId, dto.startDate(), dto.endDate());

		if (checkOverlap)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Đã có khuyến mại được thiết lập trong khoảng thời gian này");

		Product product = productService.getProductEntityById(productId);
		PromoPrice promoPrice = new PromoPrice(product, dto.promoName(), dto.promoPrice(), dto.startDate(),
				dto.endDate());

		return promoPriceRepository.save(promoPrice);
	}

	@Override
	public Page<SellPrice> getAllSellPriceByProduct(String productId, String sortBy, String orderBy, int page,
			int size) {
		List<String> allowedFields = List.of("startDate", "price");
		if (!allowedFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}
		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);
		Pageable pageable = PageRequest.of(page, size, sort);
		return sellPriceRepository.findAll(SellPriceSpecification.hasProductId(productId), pageable);
	}

	@Override
	public Page<PromoPrice> getAllPromoPriceByProduct(String productId, String sortBy, String orderBy, int page,
			int size) {
		List<String> allowedFields = List.of("startDate", "price");
		if (!allowedFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}
		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);
		Pageable pageable = PageRequest.of(page, size, sort);
		return promoPriceRepository.findAll(PromoPriceSpecification.hasProductId(productId), pageable);
	}

	@Override
	public Page<PurchasePrice> getAllPurchasePriceByProduct(String productId, String sortBy, String orderBy, int page,
			int size) {
		List<String> allowedFields = List.of("startDate", "price");
		if (!allowedFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}
		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);
		Pageable pageable = PageRequest.of(page, size, sort);
		return purchasePriceRepository.findAll(PurchasePriceSpecification.hasProductId(productId), pageable);
	}

}

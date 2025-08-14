package com.example.demo.price.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.price.entity.PromoPrice;

public class PromoPriceSpecification {

	public static Specification<PromoPrice> hasProductId(String productId) {
		return (root, query, cb) -> cb.equal(root.get("product").get("id"), productId);
	}
	
	public static Specification<PromoPrice> hasManualStatus(PromoPrice.ManualStatus status) {
	    return (root, query, cb) -> cb.equal(root.get("manualStatus"), status);
	}
	
	public static Specification<PromoPrice> isActive() {
	    return (root, query, cb) -> {
	        LocalDateTime now = LocalDateTime.now();
	        return cb.and(
	            cb.lessThanOrEqualTo(root.get("startDate"), now),
	            cb.greaterThan(root.get("endDate"), now),
	            cb.equal(root.get("manualStatus"), PromoPrice.ManualStatus.ENABLED)
	        );
	    };
	}
	
	public static Specification<PromoPrice> hasOverlap(LocalDateTime startDate, LocalDateTime endDate) {
	    return (root, query, cb) -> cb.or(
	    		cb.between(cb.literal(startDate), root.get("startDate"), root.get("endDate")),
	    		cb.between(cb.literal(endDate), root.get("startDate"), root.get("endDate"))
	    );
	}
	
}

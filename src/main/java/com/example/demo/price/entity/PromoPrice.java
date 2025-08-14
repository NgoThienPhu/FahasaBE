package com.example.demo.price.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.product.entity.Product;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@DiscriminatorValue(value = "PROMO")
public class PromoPrice extends ProductPrice {
	
	@Column(name = "promo_name", nullable = false)
	private String promoName;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "manual_status" ,nullable = false)
    private ManualStatus manualStatus = ManualStatus.ENABLED;

	public PromoPrice(Product product, String promoName, BigDecimal price, LocalDateTime startDate, LocalDateTime endDate) {
		this.product = product;
		this.promoName = promoName;
		this.price = price;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public enum ManualStatus {
	    ENABLED, DISABLED
	}
	
	public enum PromoState {
        UPCOMING, ACTIVE, PAUSED, EXPIRED
    }
	
	public PromoState getCurrentState() {
        LocalDateTime now = LocalDateTime.now();

        if (manualStatus == ManualStatus.DISABLED) {
            return PromoState.PAUSED;
        }
        if (now.isBefore(startDate)) {
            return PromoState.UPCOMING;
        }
        if (now.isAfter(endDate)) {
            return PromoState.EXPIRED;
        }
        return PromoState.ACTIVE;
    }

	@PrePersist
	public void onCreate() {
		super.onCreate();
		if (endDate == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày kế thúc giá khuyến mại là bắt buộc");

		if (endDate.isBefore(this.startDate))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Ngày kế thúc giá khuyến mại phải sau ngày bắt đầu");
	}

}

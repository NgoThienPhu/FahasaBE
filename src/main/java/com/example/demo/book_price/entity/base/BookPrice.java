package com.example.demo.book_price.entity.base;

import java.math.BigDecimal;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.example.demo.book.entity.Book;
import com.example.demo.util.entity.BaseEntity;
import com.example.demo.util.exception.CustomException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "book_price")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "price_type")
public abstract class BookPrice extends BaseEntity {

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id", nullable = false)
	protected Book book;

	@Column(name = "price", nullable = false)
	@DecimalMin(value = "1000", message = "Giá phải > 1000")
	protected BigDecimal price;

	@Column(name = "effective_from", nullable = false)
	protected LocalDateTime effectiveFrom;

	@Column(name = "effective_to", nullable = true)
	protected LocalDateTime effectiveTo;
	
	public void setEffectiveTo(LocalDateTime effectiveTo) {
		if (effectiveTo != null && this.effectiveFrom.isAfter(effectiveTo)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Ngày hiệu lực kết thúc phải sau ngày hiệu lực bắt đầu");
		}
		this.effectiveTo = effectiveTo;
	}

	public BookPrice(Book book, BigDecimal price, LocalDateTime effectiveFrom,
			LocalDateTime effectiveTo) {
		if (price.compareTo(BigDecimal.valueOf(1000)) <= 0) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Giá phải >= 1000");
		}
		if (effectiveTo != null && effectiveFrom.isAfter(effectiveTo)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Ngày hiệu lực bắt đầu phải trước ngày hiệu lực kết thúc");
		}
		this.book = book;
		this.price = price;
		this.effectiveFrom = effectiveFrom;
		this.effectiveTo = effectiveTo;
	}

}

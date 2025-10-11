package com.example.demo.book_price.base;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.book.entity.Book;
import com.example.demo.util.base.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_price")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "price_type")
public abstract class BookPrice extends BaseEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    protected Book book;

	@Column(name = "amount", nullable = false)
	protected BigDecimal amount;
	
	@Column(name = "effective_from", nullable = false)
	protected LocalDateTime effectiveFrom;
	
	@Column(name = "effective_to", nullable = false)
	protected LocalDateTime effectiveTo;
	
}

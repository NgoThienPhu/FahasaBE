package com.example.demo.book.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.book_image.entity.BookImage;
import com.example.demo.book_price.base.BookPrice;
import com.example.demo.category.entity.Category;
import com.example.demo.util.base.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "book")
public class Book extends BaseEntity {

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "author", nullable = false)
	private String author;
	
	@Column(name = "publisher", nullable = false)
	private String publisher;
	
	@Column(name = "isbn", nullable = false)
	private String isbn;
	
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "book")
	private List<BookPrice> bookPrices = new ArrayList<>();
	
	@Column(name = "publish_date", nullable = false)
	private LocalDate publishDate;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "book")
	private List<BookImage> images = new ArrayList<>();

}

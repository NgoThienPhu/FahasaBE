package com.example.demo.book_image.entity;

import com.example.demo.book.entity.Book;
import com.example.demo.util.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "book_image")
public class BookImage extends BaseEntity {

	@Column(name = "url", nullable = false)
	private String url;
	
	@Column(name = "public_id", nullable = false, unique = true)
	private String publicId;

	@Column(name = "is_primary", nullable = false)
	private Boolean isPrimary;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "book_id", nullable = false)
	private Book book;

}
